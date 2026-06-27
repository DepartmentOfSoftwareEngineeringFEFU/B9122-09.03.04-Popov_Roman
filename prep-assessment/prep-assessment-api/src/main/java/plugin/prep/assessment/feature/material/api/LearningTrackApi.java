package plugin.prep.assessment.feature.material.api;

import java.util.*;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.*;
import org.springdoc.core.annotations.*;
import org.springframework.web.bind.annotation.*;

import plugin.prep.assessment.feature.material.dto.*;
import plugin.prep.assessment.feature.material.dto.learningTrack.*;

@Tag(name = "Learning Track Api", description = "Направления изучения")
public interface LearningTrackApi {

    @PostMapping("/learning-tracks")
    @Operation(summary = "Создание направления изучения")
    LearningTrackResponse create(@RequestBody LearningTrackCreateRequest request);

    @GetMapping("/learning-tracks")
    @Operation(summary = "Получение всех направлений изучения")
    List<LearningTrackResponse> getAll();

    @GetMapping("/learning-tracks/{id}")
    @Operation(summary = "Получение направления изучения по id")
    LearningTrackResponse getById(@PathVariable Long id);

    @PutMapping("/learning-tracks/{id}")
    @Operation(summary = "Обновление направления изучения")
    LearningTrackResponse update(
        @PathVariable Long id,
        @RequestBody LearningTrackUpdateRequest request
    );

    @DeleteMapping("/learning-tracks/{id}")
    @Operation(summary = "Удаление направления изучения")
    void delete(@PathVariable Long id);

}
