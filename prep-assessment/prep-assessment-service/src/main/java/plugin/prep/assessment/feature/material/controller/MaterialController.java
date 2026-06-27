package plugin.prep.assessment.feature.material.controller;

import lombok.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.*;
import org.springframework.web.bind.annotation.*;

import plugin.prep.assessment.feature.material.api.*;
import plugin.prep.assessment.feature.material.dto.*;
import plugin.prep.assessment.feature.material.dto.material.*;
import plugin.prep.assessment.feature.material.mapper.*;
import plugin.prep.assessment.feature.material.service.*;

@RestController
@RequiredArgsConstructor
public class MaterialController implements MaterialApi {

    private final MaterialService materialService;

    private final UserMaterialService userMaterialService;

    private final UserMaterialMapper userMaterialMapper;

    @Override
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public PageDto<MaterialGetResponse> getMaterialWithFilters(MaterialGetRequest request) {
        var response = materialService.getWithFilters(request);
        return response;
    }

    @Override
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public PageDto<MaterialTopicGetResponse> getMaterialTopics(MaterialGetTopicsRequest request) {
        return materialService.getTopics(request);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public MaterialCreateResponse createMaterial(MaterialUploadRequest request) {
        return materialService.create(request, request.getFile());
    }

    @Override
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public MaterialSetLikeResponse setLike(MaterialSetLikeRequest request) {
        var userMaterialEntity = userMaterialService.setLike(
            request.getUserId(),
            request.getMaterialId(),
            request.getIsLiked()
        );

        var response = userMaterialMapper.toDto(userMaterialEntity);
        return response;
    }

    @Override
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public MaterialGetResponse getMaterialById(Long id) {
        return materialService.getById(id);
    }

    @Override
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteMaterial(Long id) {
        materialService.delete(id);
    }

}
