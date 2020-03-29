package com.application.huawei.service;

import com.application.huawei.dao.ProductDAO;
import com.application.huawei.pojo.Category;
import com.application.huawei.pojo.Product;
import com.application.huawei.util.PageUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @Auther: 10199
 * @Date: 2019/10/23 18:18
 * @Description:
 */

@Service
@CacheConfig(cacheNames="products")
public class ProductService {
    @Resource
    ProductDAO productDAO;
    @Resource
    CategoryService categoryService;
    @Resource
    OrderItemService orderItemService;
    @Resource
    ReviewService reviewService;

    @CacheEvict(allEntries=true)
    public void add(Product bean) {
        productDAO.save(bean);
    }

    @CacheEvict(allEntries=true)
    public void delete(Product bean) {
        productDAO.delete(bean);
    }

    @Cacheable(key="'products-one-'+ #p0")
    public Product get(int id) {
        Optional<Product> op = productDAO.findById(id);
        Product p = null;
        if (op.isPresent()) {
            p = op.get();
        }
        return p;
    }

    @CacheEvict(allEntries=true)
    public void update(Product bean) {
        productDAO.save(bean);
    }

    @Cacheable(key="'products-cid-'+#p0+'-page-'+#p1 + '-' + #p2 ")
    public PageUtil<Product> list(int cid, int start, int size, int navigatePages) {
        Category category = categoryService.get(cid);
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(start, size, sort);
        Page<Product> pageFromJPA =productDAO.findByCategory(category,pageable);
        return new PageUtil<>(pageFromJPA,navigatePages);
    }

    public List<Product> search(String keyword, int start, int size){
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(start, size, sort);
        return productDAO.findByNameLike("%" + keyword + "%", pageable);
    }

    @CacheEvict(allEntries = true)
    public void setSaleAndReviewNumber(Product product){
        int saleCount = orderItemService.getSaleCount(product);
        product.setSaleCount(saleCount);
        int reviewCount = reviewService.getCount(product);
        product.setReviewCount(reviewCount);
    }

    public void setSaleAndReviewNumber(List<Product> products) {
        for (Product product: products){
            setSaleAndReviewNumber(product);
        }
    }


}
