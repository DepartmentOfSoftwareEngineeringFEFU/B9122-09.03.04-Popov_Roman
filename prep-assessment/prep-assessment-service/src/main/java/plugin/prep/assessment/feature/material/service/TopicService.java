package plugin.prep.assessment.feature.material.service;

import java.util.*;

import lombok.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;

import plugin.prep.assessment.feature.material.dto.topic.*;
import plugin.prep.assessment.feature.material.entity.*;
import plugin.prep.assessment.feature.material.mapper.*;
import plugin.prep.assessment.feature.material.repository.*;
import plugin.prep.assessment.feature.recommendation.repository.*;
import plugin.prep.assessment.feature.tests.repository.*;
import plugin.prep.errors.*;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;

    private final LearningTrackRepository learningTrackRepository;

    private final MaterialRepository materialRepository;

    private final TestRepository testRepository;

    private final QuestionRepository questionRepository;

    private final UserTopicStatsRepository userTopicStatsRepository;

    private final RecommendationRepository recommendationRepository;

    public TopicEntity create(TopicCreateRequest request) {
        if (topicRepository.existsByTitle(request.getTitle())) {
            throw Exceptions.conflict("Тема уже существует: title=%s".formatted(request.getTitle()));
        }

        var topic = TopicEntity.builder()
            .title(request.getTitle())
            .description(request.getDescription())
            .build();

        return topicRepository.save(topic);
    }

    public Page<TopicEntity> getAll(TopicGetRequest request) {
        Pageable pageable = PageRequest.of(
            request.getPageNumber(),
            request.getPageSize()
        );

        return topicRepository.findAll(pageable);
    }

    public TopicEntity getById(Long id) {
        return topicRepository.findById(id)
            .orElseThrow(() -> Exceptions.notFound("Тема не найдена: id=%s".formatted(id)));
    }

    public List<TopicEntity> getByIds(List<Long> ids) {

        var topics = topicRepository.findByIdIn(ids);
        return topics;
    }

    public TopicEntity update(Long id, TopicUpdateRequest request) {
        var topic = getById(id);

        if (!topic.getTitle().equals(request.getTitle()) && topicRepository.existsByTitle(request.getTitle())) {
            throw Exceptions.conflict("Тема уже существует: title=%s".formatted(request.getTitle()));
        }

        topic.setTitle(request.getTitle());
        topic.setDescription(request.getDescription());

        return topicRepository.save(topic);
    }

    public void delete(Long id) {
        var topic = getById(id);
        validateTopicHasNoRelations(id);
        topicRepository.delete(topic);
    }

    private void validateTopicHasNoRelations(Long id) {
        Map<String, List<Long>> relatedEntityIds = new LinkedHashMap<>();

        addIfNotEmpty(relatedEntityIds, "learningTrackIds", learningTrackRepository.findIdsByTopicId(id));
        addIfNotEmpty(relatedEntityIds, "materialIds", materialRepository.findIdsByTopicId(id));
        addIfNotEmpty(relatedEntityIds, "testIds", testRepository.findIdsByTopicId(id));
        addIfNotEmpty(relatedEntityIds, "questionIds", questionRepository.findIdsByTopicId(id));
        addIfNotEmpty(relatedEntityIds, "userTopicStatsIds", userTopicStatsRepository.findIdsByTopicId(id));
        addIfNotEmpty(relatedEntityIds, "recommendationIds", recommendationRepository.findIdsByTopicId(id));

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

}
