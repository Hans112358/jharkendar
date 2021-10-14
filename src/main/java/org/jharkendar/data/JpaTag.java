package org.jharkendar.data;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "T_TAG")
@NamedQuery(name = "Tag.findAll", query = "SELECT t FROM JpaTag t")
public class JpaTag {

    @Id
    private String id;

    @Column(name = "name")
    private String name;

}
