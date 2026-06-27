package plugin.prep.assessment.feature.material.controller;

import java.util.*;

import lombok.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.*;
import org.springframework.web.bind.annotation.*;

import plugin.prep.assessment.feature.material.api.*;
import plugin.prep.assessment.feature.material.dto.learningTrack.*;
import plugin.prep.assessment.feature.material.mapper.*;
import plugin.prep.assessment.feature.material.service.*;

@RestController
@RequiredArgsConstructor
public class LearningTrackController implements LearningTrackApi {

    private final LearningTrackService learningTrackService;

    private final LearningTrackMapper learningTrackMapper;

    private final PageMapper pageMapper;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public LearningTrackResponse create(LearningTrackCreateRequest request) {
        var learningTrack = learningTrackService.create(request);
        return learningTrackMapper.toDto(learningTrack);
    }

    @Override
    public List<LearningTrackResponse> getAll() {
        var learningTracks = learningTrackService.getAll();
        var resp = learningTracks.stream()
            .map(learningTrackMapper::toDto)
            .toList();
        return resp;
    }

    @Override
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public LearningTrackResponse getById(Long id) {
        var learningTrack = learningTrackService.getById(id);
        return learningTrackMapper.toDto(learningTrack);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public LearningTrackResponse update(Long id, LearningTrackUpdateRequest request) {
        var learningTrack = learningTrackService.update(id, request);
        return learningTrackMapper.toDto(learningTrack);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(Long id) {
        learningTrackService.delete(id);
    }

}
