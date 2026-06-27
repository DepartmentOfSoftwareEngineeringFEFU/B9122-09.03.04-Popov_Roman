package plugin.prep.assessment.feature.material.api;

import io.swagger.v3.oas.annotations.*;
import org.springdoc.core.annotations.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import plugin.prep.assessment.feature.material.dto.*;
import plugin.prep.assessment.feature.material.dto.material.*;

public interface MaterialApi {

    @GetMapping("/materials")
    @Operation(description = "Получение подтем темы")
    PageDto<MaterialGetResponse> getMaterialWithFilters(@ModelAttribute @ParameterObject MaterialGetRequest request);

    @GetMapping("/materials/topics")
    @Operation(description = "Получение тем")
    PageDto<MaterialTopicGetResponse> getMaterialTopics(
        @ModelAttribute @ParameterObject MaterialGetTopicsRequest request
    );

    @PostMapping(
        value = "/materials",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(description = "Создание материала")
    MaterialCreateResponse createMaterial(@ModelAttribute MaterialUploadRequest request);

    @PostMapping("/materials/setLike")
    @Operation(description = "Поставить лайк материалу")
    MaterialSetLikeResponse setLike(@RequestBody MaterialSetLikeRequest request);

    @GetMapping("/materials/{id}")
    @Operation(description = "Получение материала по id")
    MaterialGetResponse getMaterialById(@PathVariable Long id);

    @DeleteMapping("/materials/{id}")
    @Operation(description = "Удаление материала")
    void deleteMaterial(@PathVariable Long id);

}
