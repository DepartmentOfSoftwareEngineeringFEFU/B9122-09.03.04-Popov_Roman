package plugin.prep.assessment.feature.material.api;

import java.util.*;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.*;
import org.springdoc.core.annotations.*;
import org.springframework.web.bind.annotation.*;

import plugin.prep.assessment.feature.material.dto.*;
import plugin.prep.assessment.feature.material.dto.topic.*;

@Tag(name = "Topic Api", description = "Темы")
public interface TopicApi {

    @PostMapping("/topics")
    @Operation(summary = "Создание темы")
    TopicResponse create(@RequestBody TopicCreateRequest request);

    @GetMapping("/topics")
    @Operation(summary = "Получение всех тем с пагинацией")
    PageDto<TopicResponse> getAll(@ModelAttribute @ParameterObject TopicGetRequest request);

    @GetMapping("/topics/{id}")
    @Operation(summary = "Получение темы по id")
    TopicResponse getById(@PathVariable Long id);

    @GetMapping("/topics/by-ids")
    @Operation(summary = "получение тем по их id")
    TopicGetByIdsResponse getByIds(@RequestParam List<Long> ids);

    @PutMapping("/topics/{id}")
    @Operation(summary = "Обновление темы")
    TopicResponse update(
        @PathVariable Long id,
        @RequestBody TopicUpdateRequest request
    );

    @DeleteMapping("/topics/{id}")
    @Operation(summary = "Удаление темы")
    void delete(@PathVariable Long id);

}
