package com.matrix.demo.dao.jpa.test2.repository;

import com.matrix.demo.dao.jpa.test2.entity.Test2Entity;
import matrix.module.jdbc.annotation.TargetDataSource;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author wangcheng
 * @date 2019/6/6
 */
@TargetDataSource(value = "db1")
public interface Test2Repository extends PagingAndSortingRepository<Test2Entity, Long>, JpaSpecificationExecutor<Test2Entity> {

    List<Test2Entity> findAllByBbbEquals(String bbb);
}
