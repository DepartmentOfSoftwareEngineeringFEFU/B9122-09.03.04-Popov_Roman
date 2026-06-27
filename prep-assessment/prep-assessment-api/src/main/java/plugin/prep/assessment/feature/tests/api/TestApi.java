package plugin.prep.assessment.feature.tests.api;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.*;
import org.springdoc.core.annotations.*;
import org.springframework.web.bind.annotation.*;

import plugin.prep.assessment.feature.tests.dto.page.*;
import plugin.prep.assessment.feature.tests.dto.test.*;

import java.util.List;

@Tag(name = "Test Api", description = "Тесты")
public interface TestApi {

    @PostMapping("/tests")
    @Operation(summary = "Создание теста")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Успешный ответ c  созданным тестом",
            content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TestResponse.class)
                )
            }
        )
    })
    TestResponse create(@RequestBody TestCreateRequest request);

    @GetMapping("/tests")
    @Operation(summary = "Получение всех тестов с пагинацией")
    PageDto<TestResponse> getAll(@ModelAttribute @ParameterObject TestGetDto request);

    @GetMapping("/tests/test/learning-track/{id}")
    TestResponse getTestByLearningTrack(@PathVariable Long id);

    @GetMapping("/tests/by-ids")
    TestGetByIdsResponse getTestsByIds(@RequestParam List<Long> ids);

    @DeleteMapping("/tests/{id}")
    @Operation(summary = "Удаление теста")
    void delete(@PathVariable Long id);

}
