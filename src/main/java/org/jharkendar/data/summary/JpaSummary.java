package org.jharkendar.data.summary;

import lombok.Data;
import org.jharkendar.data.tag.JpaTag;
import org.jharkendar.data.topic.JpaTopic;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "T_SUMMARY")
public class JpaSummary {
    @Id
    private String id;

    @Column(name = "title")
    private String title;

    @Column(name = "text")
    private String text;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_name")
    private JpaTopic topic;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "T_ENTITY_TAG",
            joinColumns = @JoinColumn(name = "entity_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<JpaTag> tags = new HashSet<>();
}


