package com.matrix.demo.dao.jpa.test2.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author WangCheng
 */
@Data
@Accessors(chain = true)
@Entity
@Table(name = "test")
public class Test2Entity {

    @Id
    @Column(name = "bbb")
    private String bbb;

}
