package plugin.prep.assessment.feature.material.repository;

import java.util.*;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.*;

import plugin.prep.assessment.feature.material.entity.*;

@Repository
public interface UserMaterialRepository extends JpaRepository<UserMaterialEntity, Long> {

    UserMaterialEntity findByUserIdAndMaterialId(Long userId, Long materialId);

    @Query("""
            select um.material.id
            from user_materials um
            where um.userId = :userId
            and um.isLiked = true
        """)
    Set<Long> findLikedMaterialIdsByUserId(Long userId);

    @Modifying
    @Query(value = """
        delete from user_materials
        where material_id = :materialId
        """, nativeQuery = true)
    void deleteByMaterialId(@Param("materialId") Long materialId);

}
