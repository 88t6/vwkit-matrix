package com.matrix.demo.dao.jpa.test2.repository;

import com.matrix.demo.dao.jpa.test2.entity.Test2Entity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wangcheng
 * @date 2019/6/6
 */
@Transactional
public interface Test2Repository extends PagingAndSortingRepository<Test2Entity, Long>, JpaSpecificationExecutor<Test2Entity> {
}
