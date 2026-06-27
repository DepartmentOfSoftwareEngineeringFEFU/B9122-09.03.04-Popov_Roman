package plugin.prep.auth.client.learningtrack;

import java.util.*;

import org.springframework.cloud.openfeign.*;
import org.springframework.web.bind.annotation.*;

import plugin.prep.assessment.feature.material.dto.*;
import plugin.prep.assessment.feature.material.dto.learningTrack.*;

@FeignClient(
    name = "assessmentClient",
    url = "${assessment.client.url}"
)
public interface LearningTracksClient  {

    @GetMapping("/learning-tracks")
    List<LearningTrackResponse> getAll();

}
