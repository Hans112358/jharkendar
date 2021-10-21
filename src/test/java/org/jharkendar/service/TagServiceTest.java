package org.jharkendar.service;

import org.jharkendar.data.summary.JpaSummary;
import org.jharkendar.data.summary.SummaryRepository;
import org.jharkendar.data.tag.JpaTag;
import org.jharkendar.data.tag.TagRepository;
import org.jharkendar.rest.tag.PublicTagDto;
import org.jharkendar.util.exception.TagNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @Mock
    TagRepository tagRepository;

    @Mock
    SummaryRepository summaryRepository;


    TagService sut;

    @BeforeEach
    void setUp() {
        sut = new TagService(tagRepository, summaryRepository);
    }

    @Nested
    class CreateShould {

        @Test
        void create_tag() {

            String uuid = sut.create("name");

            JpaTag expectedTag = new JpaTag(uuid, "name");
            verify(tagRepository).save(expectedTag);
        }
    }

    @Nested
    class UpdateShould {

        @Test
        void update_tag() {
            when(tagRepository.findById("id")).thenReturn(Optional.of(new JpaTag("id", "oldName")));

            sut.update("id", "newName");

            verify(tagRepository).save(new JpaTag("id", "newName"));
        }

        @Test
        void throw_exception_when_tag_not_found() {
            when(tagRepository.findById("id")).thenReturn(Optional.empty());

            assertThrows(TagNotFoundException.class,
                    () -> sut.update("id", "newName")
            );
        }
    }

    @Nested
    class DeleteShould {

        @Test
        void delete_tag() {
            when(tagRepository.findById("id")).thenReturn(Optional.of(new JpaTag("id", "name")));
            when(summaryRepository.findByTagId("id")).thenReturn(emptyList());

            sut.delete("id");

            verify(tagRepository).delete(new JpaTag("id", "name"));
        }

        @Test
        void delete_tag_from_summary() {
            // arrange
            JpaSummary summary = new JpaSummary();
            Set<JpaTag> tags = new HashSet<>(
                    asList(
                            new JpaTag("id", "name"),
                            new JpaTag("id2", "name2")
                    )
            );
            summary.setTags(tags);
            when(tagRepository.findById("id")).thenReturn(Optional.of(new JpaTag("id", "name")));
            when(summaryRepository.findByTagId("id")).thenReturn(Collections.singletonList(summary));

            // act
            sut.delete("id");

            // assert
            verify(summaryRepository).save(summary);
            assertThat(summary.getTags()).containsExactly(new JpaTag("id2", "name2"));
        }

        @Test
        void throw_exception_when_tag_not_found() {
            when(tagRepository.findById("id")).thenReturn(Optional.empty());

            assertThrows(TagNotFoundException.class,
                    () -> sut.delete("id")
            );
        }
    }

    @Nested
    class GetShould {

        @Test
        void return_tag() {
            when(tagRepository.findById("id")).thenReturn(Optional.of(new JpaTag("id", "name")));

            PublicTagDto dto = sut.getById("id");

            assertThat(dto).isEqualTo(PublicTagDto.of("id", "name"));
        }

        @Test
        void throw_exception_when_tag_not_found() {
            when(tagRepository.findById("id")).thenReturn(Optional.empty());

            assertThrows(TagNotFoundException.class,
                    () -> sut.getById("id")
            );
        }
    }

    @Nested
    class GetAllShould {

        @Test
        void get_empty_list() {
            when(tagRepository.findAll()).thenReturn(emptyList());

            List<PublicTagDto> dtos = sut.getAll();

            assertThat(dtos).isEmpty();
        }

        @Test
        void get_all_tags() {
            when(tagRepository.findAll()).thenReturn(asList(
                    new JpaTag("id", "name"),
                    new JpaTag("id2", "name2")
            ));

            List<PublicTagDto> dtos = sut.getAll();

            assertThat(dtos).containsExactlyInAnyOrder(
                    PublicTagDto.of("id", "name"),
                    PublicTagDto.of("id2", "name2")
            );
        }
    }

}