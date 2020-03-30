package com.application.huawei.controller.app;

import com.application.huawei.pojo.Product;
import com.application.huawei.pojo.ProductImage;
import com.application.huawei.pojo.PropertyValue;
import com.application.huawei.pojo.Review;
import com.application.huawei.service.ProductImageService;
import com.application.huawei.service.ProductService;
import com.application.huawei.service.PropertyValueService;
import com.application.huawei.service.ReviewService;
import com.application.huawei.util.ResultUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * App 产品相关接口
 *
 * @Author: hpj
 * @Date: 2020/3/30 11:55
 */

@RestController
public class AppProductController {
    @Resource
    PropertyValueService propertyValueService;

    @Resource
    ProductService productService;

    @Resource
    ReviewService reviewService;

    @Resource
    ProductImageService productImageService;


    @GetMapping("/frontProduct/{pid}")
    public Object product(@PathVariable("pid") int pid) {
        Product product = productService.get(pid);

        List<ProductImage> productSingleImages = productImageService.listSingleProductImages(product);
        List<ProductImage> productDetailImages = productImageService.listDetailProductImages(product);
        product.setProductSingleImages(productSingleImages);
        product.setProductDetailImages(productDetailImages);

        List<PropertyValue> pvs = propertyValueService.list(product);
        List<Review> reviews = reviewService.list(product);
        productService.setSaleAndReviewNumber(product);
        productImageService.setFirstProductImage(product);

        Map<String, Object> map = new HashMap<>();
        map.put("product", product);
        map.put("pvs", pvs);
        map.put("reviews", reviews);

        return ResultUtil.success(map);
    }



    @PostMapping("/frontSearch")
    public Object search(String keyword) {
        if (keyword == null) {
            keyword = "";
        }
        List<Product> products = productService.search(keyword, 0, 20);
        productImageService.setFirstProductImages(products);
        productService.setSaleAndReviewNumber(products);
        return products;
    }
}
