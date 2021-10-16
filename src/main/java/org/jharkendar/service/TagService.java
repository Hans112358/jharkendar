package org.jharkendar.service;

import org.jharkendar.data.tag.JpaTag;
import org.jharkendar.data.tag.TagRepository;
import org.jharkendar.rest.tag.PublicTagDto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@ApplicationScoped
public class TagService {

    @Inject
    TagRepository tagRepository;

    @Transactional
    public String create(String name) {
        JpaTag jpaTag = new JpaTag();
        jpaTag.setId(UUID.randomUUID().toString());
        jpaTag.setName(name);
        tagRepository.save(jpaTag);
        return jpaTag.getId();
    }

    @Transactional
    public void update(String id, String name) {
        JpaTag jpaTag = getByIdInternal(id);
        jpaTag.setName(name);
        tagRepository.save(jpaTag);
    }

    @Transactional
    public void delete(String id) {
        JpaTag jpaTag = getByIdInternal(id);
        tagRepository.delete(jpaTag);
    }

    @Transactional
    public PublicTagDto getById(String id) {
        return createDto(getByIdInternal(id));
    }

    private JpaTag getByIdInternal(String id) {
        return tagRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Transactional
    public List<PublicTagDto> getAll() {
        return StreamSupport.stream(tagRepository.findAll().spliterator(), false)
                .map(this::createDto)
                .collect(Collectors.toList());
    }

    private PublicTagDto createDto(JpaTag jpaTag) {
        if (jpaTag == null) {
            return null;
        }
        return PublicTagDto.of(jpaTag.getId(), jpaTag.getName());
    }
}
