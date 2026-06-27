package plugin.prep.assessment.feature.tests.controller;

import java.util.*;

import lombok.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.*;
import org.springframework.web.bind.annotation.*;

import plugin.prep.assessment.feature.tests.api.*;
import plugin.prep.assessment.feature.tests.dto.question.*;
import plugin.prep.assessment.feature.tests.mapper.*;
import plugin.prep.assessment.feature.tests.service.*;

@RestController
@RequiredArgsConstructor
public class QuestionController implements QuestionApi {

    private final QuestionService questionService;

    private final QuestionMapper questionMapper;

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public QuestionResponse create(QuestionCreateRequest request) {
        return questionService.create(request);
    }

    @Override
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<QuestionResponse> getByTestId(Long testId) {
        return questionService.getByTestId(testId);
    }

    @Override
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(Long id) {
        questionService.delete(id);
    }

}
