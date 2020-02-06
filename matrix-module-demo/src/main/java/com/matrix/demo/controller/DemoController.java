package com.matrix.demo.controller;

import com.matrix.demo.dao.mybatis.test1.model.Test1;
import com.matrix.demo.dao.mybatis.test1.service.TestService1;
import com.matrix.demo.dao.mybatis.test2.model.Test2;
import com.matrix.demo.dao.mybatis.test2.service.TestService2;
import matrix.module.common.bean.Result;
import matrix.module.common.exception.ServiceException;
import matrix.module.jdbc.annotation.DynamicTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WangCheng
 * @date 2020/2/4
 */
@RestController
@RequestMapping("/api")
@DynamicTransactional
public class DemoController {

    @Autowired
    private TestService1 testService1;

    @Autowired
    private TestService2 testService2;

    @GetMapping("/jdbc")
    public Result jdbc() {
        testService1.save(new Test1().setAaa("123"));
        testService2.save(new Test2().setBbb("456"));
        return Result.success(true);
    }

    @GetMapping("/demo")
    @DynamicTransactional
    public Result demo() {
        throw new ServiceException("aaaa");
    }
}
