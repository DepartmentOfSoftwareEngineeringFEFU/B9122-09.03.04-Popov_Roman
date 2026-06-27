package plugin.prep.assessment.feature.tests.api;

import java.util.*;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.*;
import org.springframework.web.bind.annotation.*;

import plugin.prep.assessment.feature.tests.dto.userQuestion.*;

@Tag(name = "User Question Api", description = "Ответ пользователя на вопрос")
public interface UserQuestionsApi {

    @PostMapping("/user-questions")
    @Operation(summary = "Добавление ответа пользователя на вопрос")
    UserQuestionCreateResponse create(@RequestBody UserQuestionCreateRequest request);

    @GetMapping("/user-questions/tests/{testId}")
    @Operation(summary = "Получение ответов пользователя на тест")
    List<UserQuestionGetResponse> getByTest(@PathVariable Long testId);

}
