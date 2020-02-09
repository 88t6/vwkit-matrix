package com.matrix.demo.dao.jpa.test2.service.impl;

import com.matrix.demo.dao.jpa.test2.entity.Test2Entity;
import com.matrix.demo.dao.jpa.test2.repository.Test2Repository;
import com.matrix.demo.dao.jpa.test2.service.Test2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author WangCheng
 * @date 2020/2/6
 */
@Service
public class Test2ServiceImpl implements Test2Service {

    @Autowired
    private Test2Repository test2Repository;


    @Override
    public void save(Test2Entity test2Entity) {
        test2Repository.save(test2Entity);
    }

    @Override
    public Iterable<Test2Entity> findAll() {
        return test2Repository.findAll();
    }
}
