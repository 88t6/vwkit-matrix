package com.matrix.demo.dao.mybatis.test2.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author WangCheng
 * @date 2020/2/6
 */
@Data
@Accessors(chain = true)
@TableName("test")
public class Test2 {

    private String bbb;
}
