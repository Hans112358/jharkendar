package service;


import org.jharkendar.data.JpaTopic;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class TopicService {

    @Inject
    EntityManager entityManager;

    @Transactional
    public JpaTopic create(String name) {
        JpaTopic jpaTopic = new JpaTopic();
        jpaTopic.setId(UUID.randomUUID().toString());
        jpaTopic.setName(name);
        entityManager.persist(jpaTopic);
        return jpaTopic;
    }

    @Transactional
    public void delete(String name) {
        entityManager.createNamedQuery("Topic.findAll", JpaTopic.class).getResultList()
                .stream()
                .filter(t -> t.getName().equals(name))
                .findAny()
                .ifPresent(jpaTopic -> entityManager.remove(jpaTopic));

    }

    @Transactional
    public PublicTopicDto get(String name) {
        return entityManager.createNamedQuery("Topic.findAll", JpaTopic.class).getResultList()
                .stream()
                .filter(t -> t.getName().equals(name))
                .map(this::createDto)
                .findAny()
                .orElse(null);
    }

    @Transactional
    public List<PublicTopicDto> getAll() {
        return entityManager.createNamedQuery("Topic.findAll", JpaTopic.class).getResultList()
                .stream()
                .map(this::createDto)
                .collect(Collectors.toList());
    }

    private PublicTopicDto createDto(JpaTopic jpaTopic) {
        return PublicTopicDto.of(jpaTopic.getName());
    }
}