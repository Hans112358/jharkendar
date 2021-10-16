package org.jharkendar.service;


import org.jharkendar.data.topic.JpaTopic;
import org.jharkendar.data.topic.TopicRepository;
import org.jharkendar.rest.topic.PublicTopicDto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@ApplicationScoped
public class TopicService {

    @Inject
    TopicRepository topicRepository;

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
        topicRepository.delete(jpaTopic);
    }

    @Transactional
    public PublicTopicDto getById(String id) {
        return createDto(getByIdInternal(id));
    }

    private JpaTopic getByIdInternal(String id) {
        return topicRepository.findById(id).orElseThrow(NotFoundException::new);
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