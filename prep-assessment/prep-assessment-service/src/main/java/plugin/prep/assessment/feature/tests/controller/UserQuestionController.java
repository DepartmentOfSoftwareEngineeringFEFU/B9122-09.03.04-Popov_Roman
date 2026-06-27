package plugin.prep.assessment.feature.tests.controller;

import java.util.*;

import lombok.*;
import org.springframework.security.access.prepost.*;
import org.springframework.web.bind.annotation.*;

import plugin.prep.assessment.feature.tests.api.*;
import plugin.prep.assessment.feature.tests.dto.userQuestion.*;
import plugin.prep.assessment.feature.tests.mapper.*;
import plugin.prep.assessment.feature.tests.service.*;

@RestController
@RequiredArgsConstructor
public class UserQuestionController implements UserQuestionsApi {

    private final UserQuestionService userQuestionService;

    private final UserQuestionMapper userQuestionMapper;

    @Override
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public UserQuestionCreateResponse create(UserQuestionCreateRequest request) {
        var userQuestion = userQuestionService.create(
            request.getUserId(),
            request.getQuestionId(),
            request.getAnswerId()
        );

        var response = userQuestionMapper.toDto(userQuestion);
        return response;
    }

    @Override
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<UserQuestionGetResponse> getByTest(Long testId) {
        var userQuestions = userQuestionService.getByTestId(testId);

        var response = userQuestions.stream()
            .map(userQuestionMapper::toGetDto)
            .toList();
        return response;
    }

}
