package org.jharkendar.service;


import org.jharkendar.data.summary.JpaSummary;
import org.jharkendar.data.summary.SummaryRepository;
import org.jharkendar.data.topic.JpaTopic;
import org.jharkendar.data.topic.TopicRepository;
import org.jharkendar.rest.topic.PublicTopicDto;
import org.jharkendar.util.exception.TopicNotFoundException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@ApplicationScoped
public class TopicService {

    @Inject
    TopicRepository topicRepository;

    @Inject
    SummaryRepository summaryRepository;

    @Transactional
    public String create(String name) {
        JpaTopic jpaTopic = new JpaTopic();
        jpaTopic.setId(UUID.randomUUID().toString());
        jpaTopic.setName(name);
        topicRepository.save(jpaTopic);
        return jpaTopic.getId();
    }

    @Transactional
    public void update(String id, String name) {
        JpaTopic jpaTopic = getByIdInternal(id);
        jpaTopic.setName(name);
        topicRepository.save(jpaTopic);
    }

    @Transactional
    public void delete(String id) {
        JpaTopic jpaTopic = getByIdInternal(id);

        Collection<JpaSummary> summaries = summaryRepository.findByTopicId(jpaTopic.getId());
        if (!summaries.isEmpty()) {
            throw createSummaryExistingException(jpaTopic, summaries);
        }

        topicRepository.delete(jpaTopic);
    }

    private IllegalStateException createSummaryExistingException(JpaTopic jpaTopic, Collection<JpaSummary> summaries) {
        return new IllegalStateException(
                "The following summaries still contain the Topic " +
                        jpaTopic.getName() +
                        ": " +
                        summaries.stream()
                                .map(JpaSummary::getTitle)
                                .collect(Collectors.joining(","))
        );
    }

    @Transactional
    public PublicTopicDto getById(String id) {
        return createDto(getByIdInternal(id));
    }

    private JpaTopic getByIdInternal(String id) {
        return topicRepository.findById(id).orElseThrow(() -> new TopicNotFoundException(id));
    }

    @Transactional
    public List<PublicTopicDto> getAll() {
        return StreamSupport.stream(topicRepository.findAll().spliterator(), false)
                .map(this::createDto)
                .collect(Collectors.toList());
    }

    private PublicTopicDto createDto(JpaTopic jpaTopic) {
        if (jpaTopic == null) {
            return null;
        }
        return PublicTopicDto.of(jpaTopic.getId(), jpaTopic.getName());
    }
}