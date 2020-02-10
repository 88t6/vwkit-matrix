package com.matrix.demo.dao.mybatis.test2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.matrix.demo.dao.mybatis.test2.model.Test2;

/**
 * @author WangCheng
 * @date 2020/2/6
 */
public interface TestService2 extends IService<Test2> {
    Test2 find();
}
