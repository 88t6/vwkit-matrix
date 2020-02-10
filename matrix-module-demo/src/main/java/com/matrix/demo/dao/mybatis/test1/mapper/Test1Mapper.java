package com.matrix.demo.dao.mybatis.test1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.matrix.demo.dao.mybatis.test1.model.Test1;
import matrix.module.jdbc.annotation.DynamicDataSource;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author WangCheng
 * @date 2020/2/6
 */
@Mapper
@DynamicDataSource(value = "master")
public interface Test1Mapper extends BaseMapper<Test1> {

    Test1 find();

}
