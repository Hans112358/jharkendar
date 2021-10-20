package org.jharkendar.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.jharkendar.data.summary.JpaSummary;
import org.jharkendar.data.summary.SummaryRepository;
import org.jharkendar.data.tag.JpaTag;
import org.jharkendar.data.tag.TagRepository;
import org.jharkendar.data.topic.TopicRepository;
import org.jharkendar.rest.summary.CreateSummaryDto;
import org.jharkendar.rest.summary.PublicSummaryDto;
import org.jharkendar.rest.summary.UpdateSummaryDto;
import org.jharkendar.util.exception.SummaryNotFoundException;
import org.jharkendar.util.exception.TagNotFoundException;
import org.jharkendar.util.exception.TopicNotFoundException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@NoArgsConstructor
@AllArgsConstructor
@ApplicationScoped
public class SummaryService {

    @Inject
    SummaryRepository summaryRepository;

    @Inject
    TopicRepository topicRepository;

    @Inject
    TagRepository tagRepository;

    @Transactional
    public String create(CreateSummaryDto dto) {
        JpaSummary jpaSummary = new JpaSummary();
        jpaSummary.setId(UUID.randomUUID().toString());
        jpaSummary.setCreationDate(LocalDateTime.now());
        setSummaryData(jpaSummary,
                dto.getTitle(),
                dto.getText(),
                dto.getTopicId(),
                dto.getTagIds()
        );
        summaryRepository.save(jpaSummary);
        return jpaSummary.getId();
    }

    private void setSummaryData(
            JpaSummary jpaSummary,
            String title,
            String text,
            String topicId,
            List<String> tagIds
    ) {
        jpaSummary.setTitle(title);
        jpaSummary.setText(text);
        jpaSummary.setTopic(topicRepository.findById(topicId).orElseThrow(() -> new TopicNotFoundException(topicId)));
        jpaSummary.setTags(getTagsFromIds(tagIds));
    }

    private Set<JpaTag> getTagsFromIds(List<String> tagIds) {
        if (tagIds == null) {
            return null;
        }
        return tagIds.stream()
                .map(id -> tagRepository.findById(id).orElseThrow(() -> new TagNotFoundException(id)))
                .collect(Collectors.toSet());
    }

    @Transactional
    public void update(String id, UpdateSummaryDto dto) {
        JpaSummary jpaSummary = getByIdInternal(id);
        if (jpaSummary == null) {
            throw new SummaryNotFoundException(id);
        }
        setSummaryData(
                jpaSummary,
                dto.getTitle(),
                dto.getText(),
                dto.getTopicIds(),
                dto.getTagIds()
        );

        summaryRepository.save(jpaSummary);
    }

    @Transactional
    public void delete(String id) {
        JpaSummary jpaSummary = getByIdInternal(id);
        summaryRepository.delete(jpaSummary);
    }

    @Transactional
    public PublicSummaryDto getById(String id) {
        return createDto(getByIdInternal(id));
    }

    private JpaSummary getByIdInternal(String id) {
        return summaryRepository.findById(id).orElseThrow(() -> new SummaryNotFoundException(id));
    }

    @Transactional
    public List<PublicSummaryDto> getAll() {
        return StreamSupport.stream(summaryRepository.findAll().spliterator(), false)
                .map(this::createDto)
                .collect(Collectors.toList());
    }

    private PublicSummaryDto createDto(JpaSummary jpaSummary) {
        if (jpaSummary == null) {
            return null;
        }
        return new PublicSummaryDto(
                jpaSummary.getId(),
                jpaSummary.getTitle(),
                jpaSummary.getText(),
                jpaSummary.getTopic().getId(),
                getTagIds(jpaSummary.getTags())

        );
    }

    private List<String> getTagIds(Set<JpaTag> tags) {
        if (tags == null) {
            return new ArrayList<>();
        }
        return tags.stream()
                .map(JpaTag::getId)
                .collect(Collectors.toList());
    }
}
