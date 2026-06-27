package plugin.prep.assessment.feature.material.service;

import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.stereotype.*;

import plugin.prep.assessment.feature.material.entity.*;
import plugin.prep.assessment.feature.material.repository.*;
import plugin.prep.errors.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserMaterialService {

    private final UserMaterialRepository userMaterialRepository;

    private final MaterialRepository materialRepository;

    public UserMaterialEntity setLike(Long userId, Long materialId, Boolean isLiked) {

        log.info("PARAMS userId={}, materialId={}, isLiked={}", userId, materialId, isLiked);

        var material = materialRepository
            .findById(materialId)
            .orElseThrow(() ->
                Exceptions.badRequest("Материал не найден id=%s".formatted(materialId))
            );

        var userMaterial = userMaterialRepository
            .findByUserIdAndMaterialId(
                userId,
                materialId
            );

        if (userMaterial != null && userMaterial.getIsLiked().equals(isLiked)) {
            throw Exceptions.conflict(
                "UserMaterials уже существует для этого пользователя и этого материала: userId=%s, materialId=%s"
                    .formatted(userId, materialId)
            );
        }

        if (userMaterial != null) {
            userMaterial.setIsLiked(isLiked);

            var changedUserMaterial = userMaterialRepository.save(userMaterial);
            return changedUserMaterial;
        }

        var newUserMaterial = UserMaterialEntity.builder()
            .userId(userId)
            .material(material)
            .isLiked(isLiked)
            .build();

        var savedNewUserMaterial = userMaterialRepository.save(newUserMaterial);

        return savedNewUserMaterial;
    }

}
