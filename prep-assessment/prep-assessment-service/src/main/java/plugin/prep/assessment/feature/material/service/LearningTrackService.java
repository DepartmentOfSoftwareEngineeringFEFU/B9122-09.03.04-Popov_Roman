package plugin.prep.assessment.feature.material.service;

import java.util.*;

import lombok.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;

import plugin.prep.assessment.feature.material.dto.learningTrack.*;
import plugin.prep.assessment.feature.material.entity.*;
import plugin.prep.assessment.feature.material.repository.*;
import plugin.prep.assessment.feature.tests.repository.*;
import plugin.prep.errors.*;

@Service
@RequiredArgsConstructor
public class LearningTrackService {

    private final LearningTrackRepository learningTrackRepository;

    private final TopicRepository topicRepository;

    private final TestRepository testRepository;

    public LearningTracksEntity create(LearningTrackCreateRequest request) {
        if (learningTrackRepository.existsByCode(request.getCode())) {
            throw Exceptions.conflict("Направление изучения уже существует: code=%s".formatted(request.getCode()));
        }

        var learningTrack = LearningTracksEntity.builder()
            .code(request.getCode())
            .title(request.getTitle())
            .description(request.getDescription())
            .topics(getTopics(request.getTopics()))
            .build();

        return learningTrackRepository.save(learningTrack);
    }

    public List<LearningTracksEntity> getAll() {
        return learningTrackRepository.findAll();
    }

    public LearningTracksEntity getById(Long id) {
        return learningTrackRepository.findById(id)
            .orElseThrow(() -> Exceptions.notFound("Направление изучения не найдено: id=%s".formatted(id)));
    }

    public LearningTracksEntity update(Long id, LearningTrackUpdateRequest request) {
        var learningTrack = getById(id);

        if (!learningTrack.getCode().equals(request.getCode())
            && learningTrackRepository.existsByCode(request.getCode())) {
            throw Exceptions.conflict("Направление изучения уже существует: code=%s".formatted(request.getCode()));
        }

        learningTrack.setCode(request.getCode());
        learningTrack.setTitle(request.getTitle());
        learningTrack.setDescription(request.getDescription());
        learningTrack.setTopics(getTopics(request.getTopics()));

        return learningTrackRepository.save(learningTrack);
    }

    public void delete(Long id) {
        var learningTrack = getById(id);
        validateLearningTrackHasNoRelations(id);

        learningTrackRepository.delete(learningTrack);
    }

    private void validateLearningTrackHasNoRelations(Long id) {
        Map<String, List<Long>> relatedEntityIds = new LinkedHashMap<>();

        addIfNotEmpty(relatedEntityIds, "topicIds", learningTrackRepository.findTopicIdsByLearningTrackId(id));
        addIfNotEmpty(relatedEntityIds, "testIds", testRepository.findIdsByLearningTrackId(id));

        if (!relatedEntityIds.isEmpty()) {
            throw Exceptions.conflict(
                "Удаление невозможно, пока есть связанные сущности: %s".formatted(relatedEntityIds)
            );
        }
    }

    private void addIfNotEmpty(Map<String, List<Long>> relatedEntityIds, String name, List<Long> ids) {
        if (!ids.isEmpty()) {
            relatedEntityIds.put(name, ids);
        }
    }

    private List<TopicEntity> getTopics(List<Long> topicIds) {
        if (topicIds == null || topicIds.isEmpty()) {
            return List.of();
        }

        var uniqueTopicIds = topicIds.stream()
            .distinct()
            .toList();

        var topics = topicRepository.findAllById(uniqueTopicIds);

        if (topics.size() != uniqueTopicIds.size()) {
            var foundTopicIds = topics.stream()
                .map(TopicEntity::getId)
                .toList();

            var missingTopicIds = uniqueTopicIds.stream()
                .filter(topicId -> !foundTopicIds.contains(topicId))
                .toList();

            throw Exceptions.badRequest("Темы не найдены: ids=%s".formatted(missingTopicIds));
        }

        return topics;
    }

}
