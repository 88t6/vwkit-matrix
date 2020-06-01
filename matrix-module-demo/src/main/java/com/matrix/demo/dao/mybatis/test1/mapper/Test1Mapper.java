package com.matrix.demo.dao.mybatis.test1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.matrix.demo.dao.mybatis.test1.model.Test1;
import matrix.module.jdbc.annotation.TargetDataSource;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author WangCheng
 */
@Mapper
@TargetDataSource(value = "master")
public interface Test1Mapper extends BaseMapper<Test1> {

    Test1 find();

}
