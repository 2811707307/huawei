package com.application.huawei.service;

import com.application.huawei.dao.ProductImageDAO;
import com.application.huawei.pojo.OrderItem;
import com.application.huawei.pojo.Product;
import com.application.huawei.pojo.ProductImage;
import com.application.huawei.util.SpringContextUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @Auther: 10199
 * @Date: 2019/10/24 20:03
 * @Description:
 */

@Service
@CacheConfig(cacheNames = "productImages")
public class ProductImageService {
    public static final String type_single = "single";
    public static final String type_detail = "detail";

    @Resource
    ProductImageDAO productImageDAO;
    @Resource
    ProductService productService;

    @CacheEvict(allEntries=true)
    public void add(ProductImage bean) {
        productImageDAO.save(bean);

    }

    @CacheEvict(allEntries=true)
    public void delete(ProductImage bean) {
        productImageDAO.delete(bean);
    }

    @Cacheable(key="'productImages-one-'+ #p0")
    public ProductImage get(int id) {
        Optional<ProductImage> op = productImageDAO.findById(id);
        ProductImage pi = null;
        if(op.isPresent())
            pi = op.get();
        return pi;
    }

    //根据类型查询图片
    @Cacheable(key="'productImages-single-pid-'+ #p0.id")
    public List<ProductImage> listSingleProductImages(Product product) {
        return productImageDAO.findByProductAndTypeOrderByIdDesc(product, type_single);
    }
    @Cacheable(key="'productImages-detail-pid-'+ #p0.id")
    public List<ProductImage> listDetailProductImages(Product product) {
        return productImageDAO.findByProductAndTypeOrderByIdDesc(product, type_detail);
    }


    public void setFirstProductImage(Product product) {
        ProductImageService productImageService = SpringContextUtil.getBean(ProductImageService.class);
        List<ProductImage> singleImages = listSingleProductImages(product);
        if(!singleImages.isEmpty())
            product.setFirstProductImage(singleImages.get(0));
        else
            product.setFirstProductImage(new ProductImage()); //这样做是考虑到产品还没有来得及设置图片，但是在订单后台管理里查看订单项的对应产品图片。

    }
    public void setFirstProductImages(List<Product> products) {
        for (Product product : products)
            setFirstProductImage(product);
    }

    public void setFirstProductImagesOnOrderItems(List<OrderItem> orderItems) {
        for (OrderItem orderitem: orderItems
             ) {
            setFirstProductImage(orderitem.getProduct());
        }
    }
}
