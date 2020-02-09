package com.matrix.demo.dao.jpa.test1.service;

import com.matrix.demo.dao.jpa.test1.entity.Test1Entity;

/**
 * @author WangCheng
 * @date 2020/2/6
 */
public interface Test1Service {

    void save(Test1Entity test1Entity);

    Iterable<Test1Entity> findAll();
}
