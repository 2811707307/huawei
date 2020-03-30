package com.application.huawei.controller.admin;

import com.application.huawei.pojo.Property;
import com.application.huawei.service.PropertyService;
import com.application.huawei.util.PageUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 产品属性管理 API
 *
 * @Auther: 10199
 * @Date: 2019/10/22 23:55
 * @Description: 属性管理的Controller类
 */

@RestController
public class PropertyController {
    @Resource
    PropertyService propertyService;

    @GetMapping("/categories/{cid}/properties")
    public PageUtil<Property> list(@PathVariable("cid") int cid,
                                   @RequestParam(value = "start", defaultValue = "0") int start,
                                   @RequestParam(value = "size", defaultValue = "10") int size) {
        start = start<0?0:start;
        PageUtil<Property> page = propertyService.list(cid, start, size, 8);
        return page;
    }

    @PostMapping("/properties")
    public Object add(@RequestBody Property bean) throws Exception{
        propertyService.add(bean);
        return bean;
    }

    @DeleteMapping("/properties/{id}")
    public String delete(@PathVariable("id") Property bean) throws Exception{
        propertyService.delete(bean);
        return null;
    }

    @PutMapping("/properties")
    public Object update(@RequestBody Property bean) throws Exception{
        propertyService.update(bean);
        return bean;
    }

    @GetMapping("/properties/{id}")
    public Object get(@PathVariable int id) throws Exception{
        Property bean = propertyService.get(id);
        return bean;
    }
}
