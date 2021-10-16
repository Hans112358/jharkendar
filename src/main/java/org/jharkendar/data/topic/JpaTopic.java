package org.jharkendar.data.topic;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "T_TOPIC")
public class JpaTopic {

    @Id
    private String id;

    @Column
    private String name;

}
