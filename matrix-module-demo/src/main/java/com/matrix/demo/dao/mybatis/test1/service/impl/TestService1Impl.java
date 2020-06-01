package com.matrix.demo.dao.mybatis.test1.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.matrix.demo.dao.mybatis.test1.mapper.Test1Mapper;
import com.matrix.demo.dao.mybatis.test1.model.Test1;
import com.matrix.demo.dao.mybatis.test1.service.TestService1;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author WangCheng
 */
@Service
public class TestService1Impl extends ServiceImpl<Test1Mapper, Test1> implements TestService1 {

    @Resource
    private Test1Mapper test1Mapper;

    @Override
    public Test1 find() {
        return getBaseMapper().find();
    }
}
