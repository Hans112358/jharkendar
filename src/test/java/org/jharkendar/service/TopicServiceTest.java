package org.jharkendar.service;

import org.jharkendar.data.summary.JpaSummary;
import org.jharkendar.data.summary.SummaryRepository;
import org.jharkendar.data.topic.JpaTopic;
import org.jharkendar.data.topic.TopicRepository;
import org.jharkendar.rest.topic.PublicTopicDto;
import org.jharkendar.util.exception.TopicNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TopicServiceTest {

    @Mock
    TopicRepository topicRepository;

    @Mock
    SummaryRepository summaryRepository;


    TopicService sut;

    @BeforeEach
    void setUp() {
        sut = new TopicService(topicRepository, summaryRepository);
    }

    @Nested
    class CreateShould {

        @Test
        void create_topic() {

            String uuid = sut.create("name");

            verify(topicRepository).save(new JpaTopic(uuid, "name"));
        }
    }

    @Nested
    class UpdateShould {

        @Test
        void update_topic() {
            when(topicRepository.findById("id")).thenReturn(Optional.of(new JpaTopic("id", "oldName")));

            sut.update("id", "newName");

            verify(topicRepository).save(new JpaTopic("id", "newName"));
        }

        @Test
        void throw_exception_when_topic_not_found() {
            when(topicRepository.findById("id")).thenReturn(Optional.empty());

            assertThrows(TopicNotFoundException.class,
                    () -> sut.update("id", "newName")
            );
        }
    }

    @Nested
    class DeleteShould {

        @Test
        void delete_topic() {
            when(topicRepository.findById("id")).thenReturn(Optional.of(new JpaTopic("id", "name")));

            sut.delete("id");

            verify(topicRepository).delete(new JpaTopic("id", "name"));
        }

        @Test
        void throw_exception_when_topic_not_found() {
            when(topicRepository.findById("id")).thenReturn(Optional.empty());

            assertThrows(TopicNotFoundException.class,
                    () -> sut.delete("id")
            );
        }

        @Test
        void throw_exception_when_trying_to_delete_topic_which_is_still_used() {
            JpaSummary summary = new JpaSummary();
            JpaTopic topic = new JpaTopic("id", "name");
            summary.setTopic(topic);
            summary.setTitle("title");
            when(topicRepository.findById("id")).thenReturn(Optional.of(topic));
            when(summaryRepository.findByTopicId("id")).thenReturn(Collections.singletonList(summary));

            assertThrows(IllegalStateException.class,
                    () -> sut.delete("id")
            );
        }
    }

    @Nested
    class GetShould {

        @Test
        void return_topic() {
            when(topicRepository.findById("id")).thenReturn(Optional.of(new JpaTopic("id", "name")));

            PublicTopicDto dto = sut.getById("id");

            assertThat(dto).isEqualTo(PublicTopicDto.of("id", "name"));
        }

        @Test
        void throw_exception_when_topic_not_found() {
            when(topicRepository.findById("id")).thenReturn(Optional.empty());

            assertThrows(TopicNotFoundException.class,
                    () -> sut.getById("id")
            );
        }
    }

    @Nested
    class GetAllShould {

        @Test
        void return_empty_list() {
            List<PublicTopicDto> dtos = sut.getAll();

            assertThat(dtos).isEmpty();
        }

        @Test
        void return_all_topics() {
            when(topicRepository.findAll()).thenReturn(
                    Arrays.asList(
                            new JpaTopic("id", "name"),
                            new JpaTopic("id2", "name2")
                    )
            );

            List<PublicTopicDto> dtos = sut.getAll();

            assertThat(dtos).containsExactlyInAnyOrder(
                    PublicTopicDto.of("id", "name"),
                    PublicTopicDto.of("id2", "name2")
            );
        }

    }
}
