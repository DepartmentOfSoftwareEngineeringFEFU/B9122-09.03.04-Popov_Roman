package plugin.prep.assessment.feature.tests.controller;

import lombok.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.*;
import org.springframework.web.bind.annotation.*;

import plugin.prep.assessment.feature.tests.api.*;
import plugin.prep.assessment.feature.tests.dto.page.*;
import plugin.prep.assessment.feature.tests.dto.test.*;
import plugin.prep.assessment.feature.tests.mapper.*;
import plugin.prep.assessment.feature.tests.service.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestController implements TestApi {

    private final TestService testService;

    private final TestMapper testMapper;

    private final PageMapper pageMapper;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public TestResponse create(TestCreateRequest request) {
        return testService.create(request);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public PageDto<TestResponse> getAll(TestGetDto request) {
        var tests = testService.getAll(request);

        var response = pageMapper.toPageDto(tests, testMapper::toDto);
        return response;
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.OK)
    public TestResponse getTestByLearningTrack(Long id) {
        return testService.getByTrack(id);
    }

    @Override
    public TestGetByIdsResponse getTestsByIds(List<Long> ids) {
        return testService.getByIds(ids);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(Long id) {
        testService.delete(id);
    }

}
