package com.matrix.demo.controller;

import com.matrix.demo.dao.jpa.test1.entity.Test1Entity;
import com.matrix.demo.dao.jpa.test1.repository.Test1Repository;
import com.matrix.demo.dao.jpa.test2.entity.Test2Entity;
import com.matrix.demo.dao.jpa.test2.repository.Test2Repository;
import com.matrix.demo.dao.mybatis.test1.mapper.Test1Mapper;
import com.matrix.demo.dao.mybatis.test1.service.TestService1;
import com.matrix.demo.dao.mybatis.test2.mapper.Test2Mapper;
import com.matrix.demo.dao.mybatis.test2.service.TestService2;
import matrix.module.common.bean.Result;
import matrix.module.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author WangCheng
 * @date 2020/2/4
 */
@RestController
@RequestMapping("/api")
//@DynamicTransactional
public class DemoController {

    @Autowired
    private TestService1 testService1;

    @Autowired
    private TestService2 testService2;

    @Resource
    private Test1Mapper test1Mapper;

    @Resource
    private Test2Mapper test2Mapper;

    @Autowired
    private Test1Repository test1Repository;

    @Autowired
    private Test2Repository test2Repository;

    @GetMapping("/jdbc")
    //@DynamicTransactional
    public Result jdbc() {
        testService1.find();
        testService2.find();
        //testService1.save(new Test1().setAaa("123"));
        //testService2.save(new Test2().setBbb("456"));
        //throw new RuntimeException("123");
        return Result.success(true);
    }

    @GetMapping("/jpa")
    //@DynamicTransactional
    public Result jpa() {
        test1Repository.findAllByAaaEquals("123");
        test2Repository.findAllByBbbEquals("456");
        test1Repository.save(new Test1Entity().setAaa("abc"));
        test2Repository.save(new Test2Entity().setBbb("def"));
        //throw new RuntimeException("123");
        return Result.success(true);
    }

    @GetMapping("/demo")
    public Result demo() {
        throw new ServiceException("aaaa");
    }
}
