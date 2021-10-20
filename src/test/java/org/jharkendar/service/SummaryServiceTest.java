package org.jharkendar.service;

import org.jharkendar.data.summary.JpaSummary;
import org.jharkendar.data.summary.SummaryRepository;
import org.jharkendar.data.tag.JpaTag;
import org.jharkendar.data.tag.TagRepository;
import org.jharkendar.data.topic.JpaTopic;
import org.jharkendar.data.topic.TopicRepository;
import org.jharkendar.rest.summary.CreateSummaryDto;
import org.jharkendar.rest.summary.PublicSummaryDto;
import org.jharkendar.rest.summary.UpdateSummaryDto;
import org.jharkendar.util.exception.SummaryNotFoundException;
import org.jharkendar.util.exception.TagNotFoundException;
import org.jharkendar.util.exception.TopicNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SummaryServiceTest {

    SummaryService summaryService;

    @Mock
    SummaryRepository summaryRepository;

    @Mock
    TopicRepository topicRepository;

    @Mock
    TagRepository tagRepository;

    @Captor
    ArgumentCaptor<JpaSummary> jpaSummaryArgumentCaptor;

    @BeforeEach
    void setUp() {
        summaryService = new SummaryService(
                summaryRepository,
                topicRepository,
                tagRepository
        );
    }

    @Nested
    class CreateSummaryShould {
        @Test
        void create_summary_minimal() {
            // arrange
            JpaTopic jpaTopic = new JpaTopic();
            jpaTopic.setId("topicId");
            jpaTopic.setName("topicName");

            CreateSummaryDto dto = new CreateSummaryDto(
                    "title",
                    null,
                    "topicId",
                    null
            );

            when(topicRepository.findById("topicId")).thenReturn(Optional.of(jpaTopic));

            // act
            String id = summaryService.create(dto);

            // assert
            verify(summaryRepository).save(jpaSummaryArgumentCaptor.capture());
            JpaSummary jpaSummary = jpaSummaryArgumentCaptor.getValue();
            assertThat(id).isEqualTo(jpaSummary.getId());
            assertThat(jpaSummary.getId()).isNotNull();
            assertThat(jpaSummary.getTitle()).isEqualTo("title");
            assertThat(jpaSummary.getText()).isNull();
            assertThat(jpaSummary.getTopic()).isEqualTo(jpaTopic);
            assertThat(jpaSummary.getTags()).isNull();
            assertThat(jpaSummary.getCreationDate()).isNotNull();
        }

        @Test
        void create_summary_full() {

            // arrange
            JpaTopic jpaTopic = new JpaTopic();
            jpaTopic.setId("topicId");
            jpaTopic.setName("topicName");

            JpaTag jpaTag1 = new JpaTag();
            jpaTag1.setId("tagId1");
            jpaTag1.setName("tagName1");

            JpaTag jpaTag2 = new JpaTag();
            jpaTag2.setId("tagId2");
            jpaTag2.setName("tagName2");

            CreateSummaryDto dto = new CreateSummaryDto(
                    "title",
                    "text",
                    "topicId",
                    Arrays.asList("tagId1", "tagId2")
            );

            when(topicRepository.findById("topicId")).thenReturn(Optional.of(jpaTopic));
            when(tagRepository.findById("tagId1")).thenReturn(Optional.of(jpaTag1));
            when(tagRepository.findById("tagId2")).thenReturn(Optional.of(jpaTag2));

            // act
            String id = summaryService.create(dto);

            // assert
            verify(summaryRepository).save(jpaSummaryArgumentCaptor.capture());
            JpaSummary jpaSummary = jpaSummaryArgumentCaptor.getValue();
            assertThat(id).isEqualTo(jpaSummary.getId());
            assertThat(jpaSummary.getId()).isNotNull();
            assertThat(jpaSummary.getTitle()).isEqualTo("title");
            assertThat(jpaSummary.getText()).isEqualTo("text");
            assertThat(jpaSummary.getTopic()).isEqualTo(jpaTopic);
            assertThat(jpaSummary.getTags()).containsExactlyInAnyOrder(jpaTag1, jpaTag2);
            assertThat(jpaSummary.getCreationDate()).isNotNull();
        }
    }

    @Nested
    class UpdateSummaryShould {

        @Test
        void throw_exception_when_summary_not_found() {
            assertThrows(SummaryNotFoundException.class,
                    () -> summaryService.update("summaryId", new UpdateSummaryDto())
            );
        }

        @Test
        void throw_exception_when_topic_not_found() {
            UpdateSummaryDto dto = new UpdateSummaryDto(
                    "newTitle",
                    null,
                    "newTopicId",
                    null
            );

            when(summaryRepository.findById("summaryId")).thenReturn(Optional.of(new JpaSummary()));

            assertThrows(TopicNotFoundException.class,
                    () -> summaryService.update("summaryId", dto)
            );
        }

        @Test
        void throw_exception_when_tag_not_found() {
            UpdateSummaryDto dto = new UpdateSummaryDto(
                    "newTitle",
                    null,
                    "newTopicId",
                    Collections.singletonList("newTagId")
            );

            when(summaryRepository.findById("summaryId")).thenReturn(Optional.of(new JpaSummary()));
            when(topicRepository.findById("newTopicId")).thenReturn(Optional.of(new JpaTopic()));

            assertThrows(TagNotFoundException.class,
                    () -> summaryService.update("summaryId", dto)
            );
        }

        @Test
        void update_summary_minimal() {
            // arrange
            LocalDateTime creationDate = LocalDateTime.now();

            JpaTopic existingJpaTopic = new JpaTopic();
            existingJpaTopic.setId("existingTopicId");
            existingJpaTopic.setName("existingTopicName");

            JpaTopic newJpaTopic = new JpaTopic();
            newJpaTopic.setId("newTopicId");
            newJpaTopic.setName("newTopicName");

            Set<JpaTag> existingTags = new HashSet<>();
            existingTags.add(new JpaTag());

            JpaSummary existingJpaSummary = new JpaSummary();
            existingJpaSummary.setTopic(existingJpaTopic);
            existingJpaSummary.setText("existingText");
            existingJpaSummary.setTitle("existingTitle");
            existingJpaSummary.setId("summaryId");
            existingJpaSummary.setTags(existingTags);
            existingJpaSummary.setCreationDate(creationDate);

            UpdateSummaryDto dto = new UpdateSummaryDto(
                    "newTitle",
                    null,
                    "newTopicId",
                    null
            );

            when(summaryRepository.findById("summaryId")).thenReturn(Optional.of(existingJpaSummary));
            when(topicRepository.findById("newTopicId")).thenReturn(Optional.of(newJpaTopic));

            // act
            summaryService.update("summaryId", dto);

            // assert
            verify(summaryRepository).save(jpaSummaryArgumentCaptor.capture());
            JpaSummary jpaSummary = jpaSummaryArgumentCaptor.getValue();
            assertThat(jpaSummary.getId()).isEqualTo("summaryId");
            assertThat(jpaSummary.getTitle()).isEqualTo("newTitle");
            assertThat(jpaSummary.getText()).isNull();
            assertThat(jpaSummary.getTopic()).isEqualTo(newJpaTopic);
            assertThat(jpaSummary.getTags()).isNull();
            assertThat(jpaSummary.getCreationDate()).isEqualTo(creationDate);
        }

        @Test
        void update_summary_full() {
            // arrange
            LocalDateTime creationDate = LocalDateTime.now();

            JpaTopic existingJpaTopic = new JpaTopic();
            existingJpaTopic.setId("existingTopicId");
            existingJpaTopic.setName("existingTopicName");

            JpaTopic newJpaTopic = new JpaTopic();
            newJpaTopic.setId("newTopicId");
            newJpaTopic.setName("newTopicName");

            Set<JpaTag> existingTags = new HashSet<>();
            JpaTag existingJpaTag = new JpaTag();
            existingTags.add(existingJpaTag);

            JpaTag newJpaTag = new JpaTag();
            newJpaTag.setId("newTagId");
            newJpaTag.setName("newTagName");

            JpaSummary existingJpaSummary = new JpaSummary();
            existingJpaSummary.setTopic(existingJpaTopic);
            existingJpaSummary.setText("existingText");
            existingJpaSummary.setTitle("existingTitle");
            existingJpaSummary.setId("summaryId");
            existingJpaSummary.setTags(existingTags);
            existingJpaSummary.setCreationDate(creationDate);

            UpdateSummaryDto dto = new UpdateSummaryDto(
                    "newTitle",
                    "newText",
                    "newTopicId",
                    Collections.singletonList("newTagId")
            );

            when(summaryRepository.findById("summaryId")).thenReturn(Optional.of(existingJpaSummary));
            when(topicRepository.findById("newTopicId")).thenReturn(Optional.of(newJpaTopic));
            when(tagRepository.findById("newTagId")).thenReturn(Optional.of(newJpaTag));

            // act
            summaryService.update("summaryId", dto);

            // assert
            verify(summaryRepository).save(jpaSummaryArgumentCaptor.capture());
            JpaSummary jpaSummary = jpaSummaryArgumentCaptor.getValue();
            assertThat(jpaSummary.getId()).isEqualTo("summaryId");
            assertThat(jpaSummary.getTitle()).isEqualTo("newTitle");
            assertThat(jpaSummary.getText()).isEqualTo("newText");
            assertThat(jpaSummary.getTopic()).isEqualTo(newJpaTopic);
            assertThat(jpaSummary.getTags()).containsExactly(newJpaTag);
            assertThat(jpaSummary.getCreationDate()).isEqualTo(creationDate);
        }

    }

    @Nested
    class DeleteShould {

        @Test
        void throw_exception_when_summary_not_found() {
            assertThrows(SummaryNotFoundException.class,
                    () -> summaryService.delete("summaryId")
            );
        }

        @Test
        void delete_summary() {

            // arrange
            JpaSummary jpaSummary = new JpaSummary();
            jpaSummary.setId("summaryId");

            when(summaryRepository.findById("summaryId")).thenReturn(Optional.of(jpaSummary));

            // act
            summaryService.delete("summaryId");

            // assert
            verify(summaryRepository).delete(jpaSummary);
        }
    }

    @Nested
    class GetShould {

        @Test
        void throw_exception_when_summary_not_found() {
            assertThrows(SummaryNotFoundException.class,
                    () -> summaryService.getById("123")
            );
        }

        @Test
        void return_public_dto_full() {
            // arrange
            LocalDateTime creationDate = LocalDateTime.now();

            JpaTopic jpaTopic = new JpaTopic();
            jpaTopic.setId("topicId");

            Set<JpaTag> jpaTags = new HashSet<>();
            JpaTag jpaTag = new JpaTag();
            jpaTag.setId("tagId");
            jpaTags.add(jpaTag);

            JpaSummary jpaSummary = new JpaSummary();
            jpaSummary.setId("summaryId");
            jpaSummary.setText("text");
            jpaSummary.setTitle("title");
            jpaSummary.setCreationDate(creationDate);
            jpaSummary.setTopic(jpaTopic);
            jpaSummary.setTags(jpaTags);

            when(summaryRepository.findById("summaryId")).thenReturn(Optional.of(jpaSummary));

            // act
            PublicSummaryDto summaryDto = summaryService.getById("summaryId");

            // assert
            assertThat(summaryDto).isEqualTo(new PublicSummaryDto(
                    "summaryId",
                    "title",
                    "text",
                    "topicId",
                    Collections.singletonList("tagId")
            ));
        }

        @Test
        void return_public_dto_minimal() {
            // arrange
            LocalDateTime creationDate = LocalDateTime.now();

            JpaTopic jpaTopic = new JpaTopic();
            jpaTopic.setId("topicId");

            JpaSummary jpaSummary = new JpaSummary();
            jpaSummary.setId("summaryId");
            jpaSummary.setText("text");
            jpaSummary.setCreationDate(creationDate);
            jpaSummary.setTopic(jpaTopic);

            when(summaryRepository.findById("summaryId")).thenReturn(Optional.of(jpaSummary));

            // act
            PublicSummaryDto summaryDto = summaryService.getById("summaryId");

            // assert
            assertThat(summaryDto).isEqualTo(new PublicSummaryDto(
                    "summaryId",
                    null,
                    "text",
                    "topicId",
                    new ArrayList<>()
            ));
        }
    }

    @Nested
    class GetAllShould {

        @Test
        void return_public_dto_full() {
            // arrange
            LocalDateTime creationDate = LocalDateTime.now();

            JpaTopic jpaTopic = new JpaTopic();
            jpaTopic.setId("topicId");

            Set<JpaTag> jpaTags = new HashSet<>();
            JpaTag jpaTag = new JpaTag();
            jpaTag.setId("tagId");
            jpaTags.add(jpaTag);

            JpaSummary jpaSummary = new JpaSummary();
            jpaSummary.setId("summaryId");
            jpaSummary.setText("text");
            jpaSummary.setTitle("title");
            jpaSummary.setCreationDate(creationDate);
            jpaSummary.setTopic(jpaTopic);
            jpaSummary.setTags(jpaTags);

            when(summaryRepository.findAll()).thenReturn(Collections.singletonList(jpaSummary));

            // act
            List<PublicSummaryDto> summaryDtos = summaryService.getAll();

            // assert
            assertThat(summaryDtos).containsExactly(new PublicSummaryDto(
                    "summaryId",
                    "title",
                    "text",
                    "topicId",
                    Collections.singletonList("tagId")
            ));
        }

        @Test
        void return_public_dto_minimal() {
            // arrange
            LocalDateTime creationDate = LocalDateTime.now();

            JpaTopic jpaTopic = new JpaTopic();
            jpaTopic.setId("topicId");

            JpaSummary jpaSummary = new JpaSummary();
            jpaSummary.setId("summaryId");
            jpaSummary.setText("text");
            jpaSummary.setCreationDate(creationDate);
            jpaSummary.setTopic(jpaTopic);

            when(summaryRepository.findAll()).thenReturn(Collections.singletonList(jpaSummary));

            // act
            List<PublicSummaryDto> summaryDtos = summaryService.getAll();

            // assert
            assertThat(summaryDtos).containsExactly(new PublicSummaryDto(
                    "summaryId",
                    null,
                    "text",
                    "topicId",
                    new ArrayList<>()
            ));
        }
    }
}