package com.matrix.demo.dao.jpa.test1.repository;

import com.matrix.demo.dao.jpa.test1.entity.Test1Entity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author wangcheng
 * @date 2019/6/6
 */
public interface Test1Repository extends PagingAndSortingRepository<Test1Entity, Long>, JpaSpecificationExecutor<Test1Entity> {
}
