package com.matrix.demo.dao.mybatis.test2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.matrix.demo.dao.mybatis.test2.model.Test2;
import matrix.module.jdbc.annotation.TargetDataSource;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author WangCheng
 */
@Mapper
@TargetDataSource(value = "db1")
public interface Test2Mapper extends BaseMapper<Test2> {
    Test2 find();
}
