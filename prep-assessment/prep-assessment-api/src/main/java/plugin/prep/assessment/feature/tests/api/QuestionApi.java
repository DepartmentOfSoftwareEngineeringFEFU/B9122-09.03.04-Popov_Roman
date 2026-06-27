package plugin.prep.assessment.feature.tests.api;

import java.util.*;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.*;
import org.springframework.web.bind.annotation.*;

import plugin.prep.assessment.feature.tests.dto.question.*;

@Tag(name = "Question Api", description = "Вопросы для теста")
public interface QuestionApi {

    @PostMapping("/questions")
    @Operation(summary = "Создание вопроса")
    QuestionResponse create(@RequestBody QuestionCreateRequest request);

    @GetMapping("/questions/tests/{testId}")
    @Operation(summary = "Получение вопросов теста")
    List<QuestionResponse> getByTestId(@PathVariable Long testId);

    @DeleteMapping("/questions/{id}")
    @Operation(summary = "Удаление вопроса")
    void delete(@PathVariable Long id);

}
