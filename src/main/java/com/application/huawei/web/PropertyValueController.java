package com.application.huawei.web;

import com.application.huawei.pojo.Product;
import com.application.huawei.pojo.PropertyValue;
import com.application.huawei.service.ProductService;
import com.application.huawei.service.PropertyValueService;
import jdk.nashorn.internal.ir.ReturnNode;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Auther: 10199
 * @Date: 2019/10/29 00:03
 * @Description:
 */

@RestController
public class PropertyValueController {
    @Resource
    PropertyValueService propertyValueService;
    @Resource
    ProductService productService;

    @GetMapping("/products/{pid}/propertyValues")
    public List<PropertyValue> list(@PathVariable("pid")int pid) throws Exception{
        Product product = productService.get(pid);
        propertyValueService.init(product);
        return propertyValueService.list(product);
    }

    @PutMapping("/propertyValues")
    public Object update(@RequestBody PropertyValue bean) {
        propertyValueService.update(bean);
        return bean;
    }
}
