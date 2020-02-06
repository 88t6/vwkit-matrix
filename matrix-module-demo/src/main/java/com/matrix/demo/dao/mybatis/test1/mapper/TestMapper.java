package com.matrix.demo.dao.mybatis.test1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.matrix.demo.dao.mybatis.test1.model.Test;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author WangCheng
 * @date 2020/2/6
 */
@Mapper
public interface TestMapper extends BaseMapper<Test> {

}
