package com.application.huawei.dao;

import com.application.huawei.pojo.Category;
import com.application.huawei.pojo.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Auther: 10199
 * @Date: 2019/10/22 21:29
 * @Description:
 */

/**
 * PropertyDAO 除了继承JpaRepository提供常见
 * 的CRUD之外，还提供了根据分类进行查询的功能
 */
public interface PropertyDAO extends JpaRepository<Property, Integer> {
    Page<Property> findByCategory(Category category, Pageable pageable);
    List<Property> findByCategory(Category category);
}
