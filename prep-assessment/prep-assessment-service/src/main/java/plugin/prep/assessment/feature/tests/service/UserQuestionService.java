package plugin.prep.assessment.feature.tests.service;

import java.math.*;
import java.time.*;
import java.util.*;

import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.stereotype.*;

import plugin.prep.assessment.feature.tests.entity.*;
import plugin.prep.assessment.feature.tests.enums.*;
import plugin.prep.assessment.feature.tests.repository.*;
import plugin.prep.errors.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserQuestionService {

    private final UserQuestionRepository userQuestionRepository;

    private final QuestionRepository questionRepository;

    private final AnswerRepository answerRepository;

    private final TestRepository testRepository;

    private final UserTopicStatsRepository userTopicStatsRepository;

    //todo: протестить
    public UserQuestionEntity create(
        Long userId,
        Long questionId,
        Long answerId
    ) {
        //todo: проверка пользователя(userId)

        var question = questionRepository.findById(questionId)
            .orElseThrow(() -> Exceptions.notFound(
                ErrorCode.QUESTION_NOT_FOUND.format(questionId)));

        var answer = answerRepository.findById(answerId)
            .orElseThrow(() -> Exceptions.notFound(
                ErrorCode.ANSWER_NOT_FOUND.format(answerId)));

        boolean isCorrect = Boolean.TRUE.equals(answer.getIsCorrect());

        var userQuestion = UserQuestionEntity.builder()
            .userId(userId)
            .question(question)
            .answer(answer)
            .isCorrect(isCorrect)
            .build();

        userQuestionRepository.save(userQuestion);

        var userTopicStats = userTopicStatsRepository
            .findByUserIdAndTopicAndSubtopic(
                userId,
                question.getTopic(),
                question.getSubtopic()
            );

        var now = OffsetDateTime.now();

        if (userTopicStats == null) {
            int correct = isCorrect ? 1 : 0;
            int incorrect = isCorrect ? 0 : 1;

            userTopicStats = UserTopicStatsEntity.builder()
                .userId(userId)
                .topic(question.getTopic())
                .subtopic(question.getSubtopic())
                .totalAnswered(1)
                .correctCount(correct)
                .incorrectCount(incorrect)
                .accuracy(BigDecimal.valueOf(correct))
                .lastAnsweredAt(now)
                .build();

        } else {
            int total = userTopicStats.getTotalAnswered() + 1;

            int correct = userTopicStats.getCorrectCount();
            int incorrect = userTopicStats.getIncorrectCount();

            if (isCorrect) {
                correct++;
            } else {
                incorrect++;
            }

            BigDecimal accuracy = BigDecimal.valueOf((double) correct / total);

            userTopicStats.setTotalAnswered(total);
            userTopicStats.setCorrectCount(correct);
            userTopicStats.setIncorrectCount(incorrect);
            userTopicStats.setAccuracy(accuracy);
            userTopicStats.setLastAnsweredAt(now);

            log.info("{}", accuracy);
        }

        userTopicStatsRepository.save(userTopicStats);

        return userQuestion;
    }

    public List<UserQuestionEntity> getByTestId(Long testId) {
        var testIsExists = testRepository.existsById(testId);
        if (!testIsExists) {
            throw Exceptions.notFound(ErrorCode.TEST_NOT_FOUND.format(testId));
        }

        var userQuestionsByTestId = userQuestionRepository.findByTestId(testId);
        return userQuestionsByTestId;
    }

}
