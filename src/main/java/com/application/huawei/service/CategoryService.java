package com.application.huawei.service;

import com.application.huawei.dao.CategoryDAO;
import com.application.huawei.pojo.Category;
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
import java.util.Optional;

@Service
//开启缓存，key为categories
@CacheConfig(cacheNames = "categories")
public class CategoryService {
    @Resource
    CategoryDAO categoryDAO;

    @Cacheable(key="'categories-page-'+#p0+ '-' + #p1")
    public PageUtil<Category> list(int start, int size, int navigatePages) {
        //Sort.Direction.ASC顺序排序， .DESC逆向排序
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        //new PageRequest的方法已过时
        Pageable pageable = PageRequest.of(start, size,sort);
        Page pageFromJPA =categoryDAO.findAll(pageable);

        return new PageUtil<>(pageFromJPA,navigatePages);
    }

    @CacheEvict(allEntries=true)
    public void add(Category bean) {
        categoryDAO.save(bean);
    }

    @CacheEvict(allEntries=true)
    public void delete(Category bean) {
        categoryDAO.delete(bean);
    }

    @Cacheable(key="'categories-one-'+ #p0")
    public Category get(int id) {
        //JPA2.x新特性，Optional是一个容器对象，如果值存在，isPresent()返回一个true值，get()获取值
        Optional<Category> oc = categoryDAO.findById(id);
        Category c = null;
        if (oc.isPresent()){
            c = oc.get();
        }
        return c;
    }

    @CacheEvict(allEntries=true)
    public void update(Category bean) {
        categoryDAO.save(bean);
    }
}
