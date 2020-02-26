package com.application.huawei.dao;

import com.application.huawei.pojo.Product;
import com.application.huawei.pojo.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Auther: 10199
 * @Date: 2019/10/24 19:52
 * @Description:
 */
public interface ProductImageDAO extends JpaRepository<ProductImage, Integer> {
    List<ProductImage> findByProductAndTypeOrderByIdDesc(Product product, String type);
}
