package com.matrix.demo.dao.jpa.test1.repository;

import com.matrix.demo.dao.jpa.test1.entity.Test1Entity;
import matrix.module.jdbc.annotation.DynamicDataSource;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


/**
 * @author wangcheng
 * @date 2019/6/6
 */
@Repository
@DynamicDataSource(value = "master")
public interface Test1Repository extends PagingAndSortingRepository<Test1Entity, Long>, JpaSpecificationExecutor<Test1Entity> {
}
