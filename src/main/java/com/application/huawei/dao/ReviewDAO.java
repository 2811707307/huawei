package com.application.huawei.dao;

import com.application.huawei.pojo.Product;
import com.application.huawei.pojo.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Auther: 10199
 * @Date: 2019/11/14 19:35
 * @Description: 根据产品返回评论合集，根据产品返回评论总数
 */
public interface ReviewDAO extends JpaRepository<Review, Integer> {
    List<Review> findByProductOrderByIdDesc(Product product);
    int countByProduct(Product product);
}
