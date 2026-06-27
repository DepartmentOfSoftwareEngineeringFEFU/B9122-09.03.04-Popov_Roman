package plugin.prep.assessment.feature.tests.dto.test;

import lombok.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true)
public class TestResponse {

    private Long id;

    private String title;

    private String description;

    private String type;

    private String grade;

    private String topicTitle;

}
