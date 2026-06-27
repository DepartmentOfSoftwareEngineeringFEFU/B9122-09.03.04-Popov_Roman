package plugin.prep.assessment.feature.material.mapper;

import java.util.function.*;

import org.mapstruct.*;
import org.springframework.data.domain.*;

import plugin.prep.assessment.feature.material.dto.*;

@Mapper(componentModel = "spring", uses = {MaterialMapper.class}, implementationName = "MaterialPageMapper")
public interface PageMapper {

    default <T, R> PageDto<R> toPageDto(Page<T> page, Function<T, R> mapper) {
        if (page == null) {
            return null;
        }

        PageDto<R> dto = new PageDto<>();
        dto.setContent(page.getContent().stream().map(mapper).toList());
        dto.setPageNumber(page.getNumber());
        dto.setPageSize(page.getSize());
        dto.setTotalElements(page.getTotalElements());
        dto.setTotalPages(page.getTotalPages());
        return dto;
    }

    default <T> PageDto<T> pageToPageDto(Page<T> page) {
        return new PageDto<T>()
            .setContent(page.getContent())
            .setPageNumber(page.getNumber())
            .setPageSize(page.getSize())
            .setTotalElements(page.getTotalElements())
            .setTotalPages(page.getTotalPages());
    }

}
