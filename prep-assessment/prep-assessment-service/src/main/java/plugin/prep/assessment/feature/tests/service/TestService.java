package plugin.prep.assessment.feature.tests.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import plugin.prep.assessment.feature.material.repository.LearningTrackRepository;
import plugin.prep.assessment.feature.material.repository.TopicRepository;
import plugin.prep.assessment.feature.tests.dto.test.TestCreateRequest;
import plugin.prep.assessment.feature.tests.dto.test.TestGetByIdsResponse;
import plugin.prep.assessment.feature.tests.dto.test.TestGetDto;
import plugin.prep.assessment.feature.tests.dto.test.TestResponse;
import plugin.prep.assessment.feature.tests.entity.TestEntity;
import plugin.prep.assessment.feature.tests.enums.ErrorCode;
import plugin.prep.assessment.feature.tests.enums.TestGradeEnum;
import plugin.prep.assessment.feature.tests.enums.TypeTestEnum;
import plugin.prep.assessment.feature.tests.mapper.TestMapper;
import plugin.prep.assessment.feature.tests.repository.*;
import plugin.prep.assessment.feature.tests.specification.TestSpecification;
import plugin.prep.errors.Exceptions;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;

    private final QuestionRepository questionRepository;

    private final AnswerRepository answerRepository;

    private final UserQuestionRepository userQuestionRepository;

    private final UserTestSessionRepository userTestSessionRepository;

    private final TopicRepository topicRepository;

    private final TestMapper testMapper;

    private final LearningTrackRepository learningTrackRepository;

    public TestResponse create(TestCreateRequest request) {
        var testIsExists = testRepository.existsByTitle(request.getTitle());
        if (testIsExists) {
            throw Exceptions.conflict(ErrorCode.TEST_ALREADY_EXISTS.format(request.getTitle()));
        }

        var typeEnum = TypeTestEnum.valueOf(request.getType());
        //todo: очевидно елси будет null то 500
        var gradeEnum = TestGradeEnum.fromValue(request.getGrade());

        var topic = topicRepository.findById(request.getTopicId())
                .orElseThrow(() -> Exceptions.badRequest(
                        "Топик не найден: %s".formatted(request.getTopicId())
                ));

        var test = TestEntity.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .type(typeEnum)
                .grade(gradeEnum)
                .topic(topic)
                .build();

        if (typeEnum.equals(TypeTestEnum.DIAGNOSTIC)) {
            if (request.getLearningTrackId() == null) {
                throw Exceptions.badRequest("Для теста типа: DIAGNOSTIC обязательно указывать направление подготовки");
            }

            var learningTrack = learningTrackRepository
                    .findById(request.getLearningTrackId())
                    .orElseThrow(() -> Exceptions.badRequest(
                            "Направление подготовки не найдено: %s".formatted(request.getLearningTrackId())
                    ));

            var testByLearningTrack = testRepository.findByLearningTrackId(learningTrack.getId());
            if (testByLearningTrack != null) {
                throw Exceptions.conflict(
                        "Тест для этого направления уже создан: learningTrackId=%s"
                                .formatted(learningTrack.getId()));
            }
            test.setLearningTrack(learningTrack);
        }

        var savedTest = testRepository.save(test);
        return testMapper.toDto(savedTest);
    }

    public Page<TestEntity> getAll(TestGetDto request) {
        Pageable pageable = PageRequest.of(
                request.getPageNumber(),
                request.getPageSize()
        );

        TypeTestEnum type = request.getType() == null || request.getType().isBlank()
                ? TypeTestEnum.REGULAR
                : TypeTestEnum.valueOf(request.getType());

        Specification<TestEntity> specification = Specification
                .where(TestSpecification.hasType(type));

        var gradeFilter = request.getLevel() == null || request.getLevel().isBlank()
                ? request.getGrade()
                : request.getLevel();

        if (gradeFilter != null && !gradeFilter.isBlank()) {
            TestGradeEnum grade = TestGradeEnum.fromValue(gradeFilter);

            specification = specification.and(
                    TestSpecification.hasGrade(grade)
            );
        }

        if (request.getTopicId() != null) {
            specification = specification.and(
                    TestSpecification.hasTopicId(request.getTopicId())
            );
        }

        return testRepository.findAll(specification, pageable);
    }

    public TestResponse getByTrack(Long trackId) {

        var learningTrackIsExists = learningTrackRepository.existsById(trackId);
        if (!learningTrackIsExists) {
            throw Exceptions.notFound("Направление подготовки не найдено: trackId=%s".formatted(trackId));
        }

        var test = testRepository.findByLearningTrackId(trackId);
        if (test == null) {
            throw Exceptions.notFound("Тест для этого направления не найден: trackId=%s".formatted(trackId));
        }
        return testMapper.toDto(test);
    }

    public TestGetByIdsResponse getByIds(List<Long> ids) {
        var tests = testRepository.findByIdIn(ids);

        var titles = tests.stream()
                .map(TestEntity::getTitle)
                .toList();

        return new TestGetByIdsResponse()
                .setTitle(titles);
    }

    @Transactional
    public void delete(Long id) {
        if (!testRepository.existsById(id)) {
            throw Exceptions.notFound(ErrorCode.TEST_NOT_FOUND.format(id));
        }

        userQuestionRepository.deleteByTestId(id);
        answerRepository.deleteByTestId(id);
        questionRepository.deleteByTestId(id);
        userTestSessionRepository.deleteByTestId(id);
        testRepository.deleteById(id);
    }

}
