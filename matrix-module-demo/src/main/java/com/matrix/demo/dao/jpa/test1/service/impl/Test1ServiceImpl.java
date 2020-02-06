package com.matrix.demo.dao.jpa.test1.service.impl;

import com.matrix.demo.dao.jpa.test1.entity.Test1Entity;
import com.matrix.demo.dao.jpa.test1.repository.Test1Repository;
import com.matrix.demo.dao.jpa.test1.service.Test1Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author WangCheng
 * @date 2020/2/6
 */
@Service
public class Test1ServiceImpl implements Test1Service {

    @Autowired
    private Test1Repository test1Repository;

    @Override
    public void save(Test1Entity test1Entity) {
        test1Repository.save(test1Entity);
    }
}
