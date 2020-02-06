package com.matrix.demo.dao.mybatis.test2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.matrix.demo.dao.mybatis.test2.mapper.TestMapper2;
import com.matrix.demo.dao.mybatis.test2.model.Test2;
import com.matrix.demo.dao.mybatis.test2.service.TestService2;
import org.springframework.stereotype.Service;

/**
 * @author WangCheng
 * @date 2020/2/6
 */
@Service
public class TestService2Impl extends ServiceImpl<TestMapper2, Test2> implements TestService2 {

}
