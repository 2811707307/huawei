package com.application.huawei.service;

import com.application.huawei.dao.PropertyDAO;
import com.application.huawei.pojo.Category;
import com.application.huawei.pojo.Property;
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
 * @Date: 2019/10/22 21:44
 * @Description:
 */

@Service
@CacheConfig(cacheNames = "properties")
public class PropertyService {
    @Resource PropertyDAO propertyDAO;
    @Resource CategoryService categoryService;

    @CacheEvict(allEntries = true)
    public void add(Property bean) {
        propertyDAO.save(bean);
    }

    @CacheEvict(allEntries = true)
    public void delete(Property bean) {
        propertyDAO.delete(bean);
    }

    @Cacheable(key = "'properties-one-' + #p0")
    public Property get(int id) {
        Optional<Property> op = propertyDAO.findById(id);
        Property p = null;
        if (op.isPresent()) {
            p = op.get();
        }
        return p;
    }

    @CacheEvict(allEntries = true)
    public void update(Property bean) {
        propertyDAO.save(bean);
    }

    @Cacheable(key="'properties-cid-'+#p0+'-page-'+#p1 + '-' + #p2 ")
    public PageUtil<Property> list(int cid, int start, int size, int navigatePages) {
        //先获取父级分类
        Category category = categoryService.get(cid);

        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(start, size, sort);
        Page<Property> pageFromJPA = propertyDAO.findByCategory(category, pageable);

        return new PageUtil<>(pageFromJPA, navigatePages);
    }

    @Cacheable(key="'properties-cid-'+ #p0.id")
    public List<Property> listByCategory(Category category){
        return propertyDAO.findByCategory(category);
    }
}
