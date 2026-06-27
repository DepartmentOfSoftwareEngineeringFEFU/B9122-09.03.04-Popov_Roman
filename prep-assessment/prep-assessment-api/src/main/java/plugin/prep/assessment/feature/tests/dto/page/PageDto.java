package plugin.prep.assessment.feature.tests.dto.page;

import java.util.*;

import io.swagger.v3.oas.annotations.media.*;
import lombok.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true)
public class PageDto<T> {

    @Schema(description = "Возвращаемые элементы")
    private List<T> content;

    @Schema(description = "Номер текущей страницы (начиная с 0)")
    private int pageNumber;

    @Schema(description = "Количество элементов на странице")
    private int pageSize;

    @Schema(description = "Общее количество элементов")
    private long totalElements;

    @Schema(description = "Общее количество страниц")
    private int totalPages;

}
