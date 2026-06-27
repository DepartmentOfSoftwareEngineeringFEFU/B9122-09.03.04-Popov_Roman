package plugin.prep.assessment.feature.material.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import plugin.prep.assessment.feature.material.api.TopicApi;
import plugin.prep.assessment.feature.material.dto.PageDto;
import plugin.prep.assessment.feature.material.dto.topic.TopicCreateRequest;
import plugin.prep.assessment.feature.material.dto.topic.TopicGetByIdsResponse;
import plugin.prep.assessment.feature.material.dto.topic.TopicGetRequest;
import plugin.prep.assessment.feature.material.dto.topic.TopicResponse;
import plugin.prep.assessment.feature.material.dto.topic.TopicUpdateRequest;
import plugin.prep.assessment.feature.material.entity.TopicEntity;
import plugin.prep.assessment.feature.material.mapper.PageMapper;
import plugin.prep.assessment.feature.material.mapper.TopicMapper;
import plugin.prep.assessment.feature.material.service.TopicService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TopicController implements TopicApi {

    private final TopicService topicService;

    private final TopicMapper topicMapper;

    private final PageMapper pageMapper;

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public TopicResponse create(TopicCreateRequest request) {
        var topic = topicService.create(request);
        return topicMapper.toDto(topic);
    }

    @Override
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public PageDto<TopicResponse> getAll(TopicGetRequest request) {
        var topics = topicService.getAll(request);
        return pageMapper.toPageDto(topics, topicMapper::toDto);
    }

    @Override
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public TopicResponse getById(Long id) {
        var topic = topicService.getById(id);
        return topicMapper.toDto(topic);
    }

    @Override
    public TopicGetByIdsResponse getByIds(List<Long> ids) {
        var topics = topicService.getByIds(ids);

        var titles = topics.stream()
                .map(TopicEntity::getTitle)
                .toList();

        return new TopicGetByIdsResponse()
                .setTitle(titles);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public TopicResponse update(Long id, TopicUpdateRequest request) {
        var topic = topicService.update(id, request);
        return topicMapper.toDto(topic);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(Long id) {
        topicService.delete(id);
    }

}
