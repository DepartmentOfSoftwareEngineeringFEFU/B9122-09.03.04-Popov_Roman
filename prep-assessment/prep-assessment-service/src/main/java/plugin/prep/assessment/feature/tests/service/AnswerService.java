package plugin.prep.assessment.feature.tests.service;

import java.util.*;

import lombok.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import plugin.prep.assessment.feature.tests.entity.*;
import plugin.prep.assessment.feature.tests.enums.*;
import plugin.prep.assessment.feature.tests.repository.*;
import plugin.prep.errors.*;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;

    private final UserQuestionRepository userQuestionRepository;

    private final QuestionRepository questionRepository;

    public AnswerEntity create(
        Long questionId,
        String text,
        Boolean isCorrect,
        String explanation
    ) {
        var question = questionRepository
            .findById(questionId)
            .orElseThrow(() -> Exceptions.notFound(
                ErrorCode.QUESTION_NOT_FOUND.format(questionId)
            ));

        var answerAlreadyExists = answerRepository
            .existsByQuestionIdAndText(
                questionId,
                text
            );

        if (answerAlreadyExists) {
            throw Exceptions.conflict(
                ErrorCode.ANSWER_ALREADY_EXISTS.format(questionId, text)
            );
        }
        var answer = AnswerEntity.builder()
            .question(question)
            .text(text)
            .isCorrect(isCorrect)
            .explanation(explanation)
            .build();

        var savedAnswer = answerRepository.save(answer);
        return savedAnswer;
    }

    public List<AnswerEntity> getByQuestionId(Long questionId) {
        var questionIsExists = questionRepository.existsById(questionId);
        if (!questionIsExists) {
            throw Exceptions.notFound(ErrorCode.QUESTION_NOT_FOUND.format(questionId));
        }

        var answers = answerRepository.findByQuestionId(questionId);
        return answers;
    }

    @Transactional
    public void delete(Long id) {
        var answerIsExists = answerRepository.existsById(id);
        if (!answerIsExists) {
            throw Exceptions.notFound(ErrorCode.ANSWER_NOT_FOUND.format(id));
        }

        userQuestionRepository.deleteByAnswerId(id);
        answerRepository.deleteById(id);
    }

}
