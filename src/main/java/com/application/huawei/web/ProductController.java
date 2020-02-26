package com.application.huawei.web;

import com.application.huawei.pojo.Product;
import com.application.huawei.service.CategoryService;
import com.application.huawei.service.ProductImageService;
import com.application.huawei.service.ProductService;
import com.application.huawei.util.PageUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Auther: 10199
 * @Date: 2019/10/23 18:21
 * @Description:
 */
@RestController
public class ProductController {
    @Resource
    ProductService productService;
    @Resource
    ProductImageService productImageService;

    @GetMapping("/categories/{cid}/products")
    public PageUtil<Product> list(@PathVariable("cid") int cid, @RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "5") int size) throws Exception {
        start = start<0?0:start;
        PageUtil<Product> page =productService.list(cid, start, size,5 );
        productImageService.setFirstProductImages(page.getContent());
        return page;
    }

    @GetMapping("/products/{id}")
    public Product get(@PathVariable("id") int id) throws Exception {
        return productService.get(id);
    }

    @PostMapping("/products")
    public Object add(@RequestBody Product bean) throws Exception {
        bean.setCreateDate(new Date());
        productService.add(bean);
        return bean;
    }

    @DeleteMapping("/products/{id}")
    public String delete(@PathVariable("id") Product bean)  throws Exception {
        productService.delete(bean);
        return null;
    }

    @PutMapping("/products")
    public Object update(@RequestBody Product bean) throws Exception {
        productService.update(bean);
        return bean;
    }
}
