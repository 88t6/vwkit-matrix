package com.matrix.demo.dao.mybatis.test1.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author WangCheng
 */
@Data
@Accessors(chain = true)
@TableName("test")
public class Test1 {

    private String aaa;
}
