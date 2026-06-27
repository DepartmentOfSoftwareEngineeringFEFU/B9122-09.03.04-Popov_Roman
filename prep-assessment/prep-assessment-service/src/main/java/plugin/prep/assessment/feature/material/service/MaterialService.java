package plugin.prep.assessment.feature.material.service;

import java.io.*;
import java.nio.charset.*;
import java.util.*;

import io.minio.*;
import jakarta.transaction.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.core.io.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.multipart.*;

import plugin.prep.assessment.feature.material.dto.*;
import plugin.prep.assessment.feature.material.dto.material.*;
import plugin.prep.assessment.feature.material.entity.*;
import plugin.prep.assessment.feature.material.enums.*;
import plugin.prep.assessment.feature.material.mapper.*;
import plugin.prep.assessment.feature.material.repository.*;
import plugin.prep.errors.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MaterialService {

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
        "application/pdf",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    );

    private final MaterialRepository materialRepository;

    private final FileRepository fileRepository;

    private final MinioStorageService minioStorageService;

    private final UserMaterialRepository userMaterialRepository;

    private final MaterialMapper materialMapper;

    private final PageMapper pageMapper;

    private final TopicRepository topicRepository;

    private final LearningTrackRepository learningTrackRepository;

    public PageDto<MaterialGetResponse> getWithFilters(MaterialGetRequest request) {
        var levelEnum = MaterialLevelEnum.fromValue(request.getLevel());
        Pageable pageable = PageRequest.of(request.getPageNumber(), request.getPageSize());

        var likedMaterialIds = userMaterialRepository.findLikedMaterialIdsByUserId(request.getUserId());
        var topic = topicRepository.findById(request.getTopicId())
            .orElseThrow(() -> Exceptions.badRequest(
                "Топик не найден id=%s".formatted(request.getTopicId()))
            );

        Specification<MaterialEntity> spec = Specification.where((root, query, cb) ->
            cb.equal(root.get("topic"), topic)
        );

        if (levelEnum != null) {
            spec = spec.and((root, query, cb) ->
                cb.equal(root.get("grade"), levelEnum)
            );
        }

        if (request.getSubtopic() != null && !request.getSubtopic().isBlank()) {
            spec = spec.and((root, query, cb) ->
                cb.equal(root.get("subtopic"), request.getSubtopic())
            );
        }

        Page<MaterialEntity> materials = materialRepository.findAll(spec, pageable);

        var responses = materials.map(material -> toMaterialGetResponse(
            material,
            likedMaterialIds.contains(material.getId())
        ));

        return pageMapper.pageToPageDto(responses);
    }

    public MaterialGetResponse getById(Long id) {
        var material = materialRepository.findById(id).orElseThrow(() -> Exceptions.notFound("материал не найден"));

        var response = materialMapper.toGetDto(material);
        return response;
    }

    @Transactional
    public void delete(Long id) {
        var material = materialRepository.findById(id)
            .orElseThrow(() -> Exceptions.notFound("Материал не найден id=%s".formatted(id)));

        var file = material.getFile();

        userMaterialRepository.deleteByMaterialId(id);
        materialRepository.delete(material);
        materialRepository.flush();
        fileRepository.delete(file);
        fileRepository.flush();
        minioStorageService.delete(file.getObjectKey());
    }

    public PageDto<MaterialTopicGetResponse> getTopics(MaterialGetTopicsRequest request) {
        Pageable pageable = PageRequest.of(request.getPageNumber(), request.getPageSize());

        Specification<MaterialEntity> spec = Specification.where(null);

        if (request.getLevel() != null) {
            var levelEnum = MaterialLevelEnum.fromValue(request.getLevel());
            spec = spec.and((root, query, cb) ->
                cb.equal(root.get("grade"), levelEnum)
            );
        }

        if (request.getLearningTrackId() != null) {
            var learningTrack = learningTrackRepository
                .findById(request.getLearningTrackId())
                .orElseThrow(() -> Exceptions.badRequest(
                    "Направление обучения не найдено id=%s"
                        .formatted(request.getLearningTrackId()))
                );

            var topics = learningTrack.getTopics();

            spec = spec.and((root, query, cb) ->
                root.get("topic").in(topics)
            );
        }

        var materials = materialRepository.findAll(spec, pageable);

        var materialDtos = materials.map(materialMapper::toGetTopicDto);

        return pageMapper.pageToPageDto(materialDtos);
    }

    @Transactional
    public MaterialCreateResponse create(MaterialUploadRequest request, MultipartFile file) {
        validate(request, file);

        var topic = topicRepository.findById(request.getTopicId())
            .orElseThrow(() -> Exceptions.badRequest(
                "Топик не найден: %s".formatted(request.getTopicId())
            ));

        String extension = extractExtension(file.getOriginalFilename());
        String objectKey =
            buildObjectKey(
                topic.getTitle(), request.getLevel() != null ? request.getLevel() : "COMMON",
                extension);

        minioStorageService.upload(objectKey, file);

        FileEntity fileEntity = fileRepository.save(
            FileEntity.builder()
                .bucket(minioStorageService.getBucket())
                .objectKey(objectKey)
                .originalName(file.getOriginalFilename())
                .contentType(file.getContentType())
                .sizeBytes(file.getSize())
                .status(FileStatusEnum.UPLOADED)
                .uploadedBy(null)
                .build()
        );

        var gradeEnum = MaterialLevelEnum.fromValue(request.getLevel());

        MaterialEntity material = materialRepository.save(
            MaterialEntity.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .topic(topic)
                .subtopic(request.getSubtopic())
                .grade(gradeEnum)
                .file(fileEntity)
                .build()
        );

        return MaterialCreateResponse.builder()
            .materialId(material.getId())
            .fileId(fileEntity.getId())
            .build();
    }

    public ResponseEntity<InputStreamResource> getMaterialFile(Long fileId) {
        log.info("FILEID {}", fileId);

        var file = fileRepository.findById(fileId)
            .orElseThrow(() ->
                new PrepException("Материал не найден", HttpStatus.NOT_FOUND));

        InputStream inputStream =
            minioStorageService.download(file.getObjectKey());

        StatObjectResponse stat =
            minioStorageService.stat(file.getObjectKey());

        String contentType = file.getContentType() != null
            ? file.getContentType()
            : MediaType.APPLICATION_OCTET_STREAM_VALUE;

        String fileName = file.getOriginalName() != null
            ? file.getOriginalName()
            : "file";

        ContentDisposition disposition;

        if ("application/pdf".equals(contentType)) {
            disposition = ContentDisposition.inline()
                .filename(fileName, StandardCharsets.UTF_8)
                .build();
        } else {
            disposition = ContentDisposition.attachment()
                .filename(fileName, StandardCharsets.UTF_8)
                .build();
        }

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .contentLength(stat.size())
            .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
            .body(new InputStreamResource(inputStream));
    }

    private MaterialGetResponse toMaterialGetResponse(
        MaterialEntity material,
        Boolean isLiked
    ) {
        return new MaterialGetResponse()
            .setId(material.getId())
            .setTitle(material.getTitle())
            .setDescription(material.getDescription())
            .setFileId(material.getFile().getId())
            .setTopicTitle(material.getTopic().getTitle())
            .setSubtopic(material.getSubtopic())
            .setLevel(material.getGrade().name())
            .setIsLiked(isLiked);
    }

    private void validate(MaterialUploadRequest request, MultipartFile file) {
        if (materialRepository.existsByTitle(request.getTitle())) {
            throw new IllegalArgumentException("Материал с таким title уже существует");
        }

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Файл обязателен");
        }

        if (file.getContentType() == null || !ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
            throw new IllegalArgumentException("Недопустимый contentType: " + file.getContentType());
        }
    }

    private String buildObjectKey(String topicTitle, String level, String extension) {
        return "materials/" + sanitize(topicTitle) + "/" + sanitize(level) + "/" + UUID.randomUUID() + extension;
    }

    private String sanitize(String value) {
        return value == null
            ? "common"
            : value.trim().toLowerCase().replaceAll("[^a-zA-Z0-9_-]+", "-");
    }

    private String extractExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }

}
