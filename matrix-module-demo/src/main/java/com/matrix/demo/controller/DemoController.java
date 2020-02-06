package com.matrix.demo.controller;

import matrix.module.common.bean.Result;
import matrix.module.common.exception.ServiceException;
import matrix.module.jdbc.annotation.DynamicTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WangCheng
 * @date 2020/2/4
 */
@RestController
@RequestMapping("/api")
public class DemoController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/jdbc")
    @DynamicTransactional
    public Result jdbc() {
        jdbcTemplate.execute("insert into test (aaa) values (456)");
        jdbcTemplate.execute("insert into test (aaa) values (456)");
        return Result.success(true);
    }

    @GetMapping("/demo")
    @DynamicTransactional
    public Result demo() {
        throw new ServiceException("aaaa");
    }
}
