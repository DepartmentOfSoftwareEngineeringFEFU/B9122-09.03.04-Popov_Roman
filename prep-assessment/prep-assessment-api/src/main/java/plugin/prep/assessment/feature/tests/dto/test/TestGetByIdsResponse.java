package plugin.prep.assessment.feature.tests.dto.test;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class TestGetByIdsResponse {
    private List<String> title;
}
