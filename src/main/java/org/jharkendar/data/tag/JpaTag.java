package org.jharkendar.data.tag;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "T_TAG")
public class JpaTag {

    @Id
    private String id;

    @Column
    private String name;

}
