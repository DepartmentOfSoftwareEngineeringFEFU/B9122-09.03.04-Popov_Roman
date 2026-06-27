package plugin.prep.assessment.feature.tests.controller;

import java.util.*;

import lombok.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.*;
import org.springframework.web.bind.annotation.*;

import plugin.prep.assessment.feature.tests.api.*;
import plugin.prep.assessment.feature.tests.dto.answers.*;
import plugin.prep.assessment.feature.tests.mapper.*;
import plugin.prep.assessment.feature.tests.service.*;

@RestController
@RequiredArgsConstructor
public class AnswerController implements AnswerApi {

    private final AnswerService answerService;

    private final AnswerMapper answerMapper;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public AnswerCreateResponse create(AnswerCreateRequest request) {
        var savedAnswer = answerService.create(
            request.getQuestionId(),
            request.getText(),
            request.getIsCorrect(),
            request.getExplanation()
        );

        var response = answerMapper.toDto(savedAnswer);
        return response;
    }

    @Override
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<AnswerGetResponse> getByQuestionId(Long questionId) {
        var answers = answerService.getByQuestionId(questionId);

        var response = answers.stream()
            .map(answerMapper::toGetDto)
            .toList();
        return response;
    }

    @Override
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(Long id) {
        answerService.delete(id);
    }

}
