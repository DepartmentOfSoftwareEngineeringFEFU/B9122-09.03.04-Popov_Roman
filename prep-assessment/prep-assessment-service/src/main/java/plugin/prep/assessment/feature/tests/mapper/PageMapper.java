package plugin.prep.assessment.feature.tests.mapper;

import java.util.function.*;

import org.mapstruct.*;
import org.springframework.data.domain.*;

import plugin.prep.assessment.feature.tests.dto.page.*;

@Mapper(componentModel = "spring", uses = {TestMapper.class}, implementationName = "TestPageMapper")
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

}
