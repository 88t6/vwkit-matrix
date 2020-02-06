package com.matrix.demo.dao.mybatis.test1.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.matrix.demo.dao.mybatis.test1.mapper.TestMapper;
import com.matrix.demo.dao.mybatis.test1.model.Test;
import com.matrix.demo.dao.mybatis.test1.service.TestService1;
import org.springframework.stereotype.Service;

/**
 * @author WangCheng
 * @date 2020/2/6
 */
@Service
public class TestService1Impl extends ServiceImpl<TestMapper, Test> implements TestService1 {

}
