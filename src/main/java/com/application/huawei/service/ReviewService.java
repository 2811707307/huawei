package com.application.huawei.service;

import com.application.huawei.dao.ReviewDAO;
import com.application.huawei.pojo.Product;
import com.application.huawei.pojo.Review;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Auther: 10199
 * @Date: 2019/11/14 19:40
 * @Description:
 */
@Service
@CacheConfig(cacheNames = "reviews")
public class ReviewService {
    @Resource
    ReviewDAO reviewDAO;

    @CacheEvict(allEntries=true)
    public void add(Review review){
        reviewDAO.save(review);
    }

    @Cacheable(key="'reviews-pid-'+ #p0.id")
    public List<Review> list(Product product){
        List<Review> list = reviewDAO.findByProductOrderByIdDesc(product);
        return list;
    }

    @Cacheable(key="'reviews-count-pid-'+ #p0.id")
    public int getCount(Product product) {
        return reviewDAO.countByProduct(product);
    }
}
