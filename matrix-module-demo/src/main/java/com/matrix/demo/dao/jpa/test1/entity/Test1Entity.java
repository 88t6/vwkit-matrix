package com.matrix.demo.dao.jpa.test1.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author WangCheng
 * @date 2020/2/6
 */
@Data
@Accessors(chain = true)
@Entity
@Table(name = "test")
public class Test1Entity {

    @Id
    @Column(name = "aaa")
    private String aaa;

}
