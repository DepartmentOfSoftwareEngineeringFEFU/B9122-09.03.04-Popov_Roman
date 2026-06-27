package plugin.prep.assessment.feature.material.dto.topic;

import java.util.*;

import lombok.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true)
public class TopicGetByIdsResponse {

    private List<String> title;

}
