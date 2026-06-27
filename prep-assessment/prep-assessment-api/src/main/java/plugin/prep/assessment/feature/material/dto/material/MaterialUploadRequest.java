package plugin.prep.assessment.feature.material.dto.material;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.*;

@Data
public class MaterialUploadRequest {

    private String title;

    private String description;

    private Long topicId;

    private String subtopic;

    private String level;

    @NotEmpty
    private MultipartFile file;

}
