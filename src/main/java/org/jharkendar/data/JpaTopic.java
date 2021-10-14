package org.jharkendar.data;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "T_TOPIC")
@NamedQuery(name = "Topic.findAll", query = "SELECT t FROM JpaTopic t")
public class JpaTopic {

    @Id
    private String id;

    @Column(name = "name")
    private String name;

}
