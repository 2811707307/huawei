package com.application.huawei.dao;


import java.util.List;

import com.application.huawei.pojo.Category;
import com.application.huawei.pojo.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductDAO extends JpaRepository<Product,Integer>{
    Page<Product> findByCategory(Category category, Pageable pageable);
    List<Product> findByNameLike(String keyword, Pageable pageable);
}