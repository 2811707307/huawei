package com.application.huawei.service;

import com.application.huawei.dao.PropertyValueDAO;
import com.application.huawei.pojo.Product;
import com.application.huawei.pojo.Property;
import com.application.huawei.pojo.PropertyValue;
import com.application.huawei.util.SpringContextUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Auther: 10199
 * @Date: 2019/10/28 22:53
 * @Description:
 * 提供修改查询和初始化
 * 对于产品属性值，没有增加，只有修改。所以通过初始化来自动增加
 * 根据产品获取分类，然后获取这个分类下所有属性集合
 * 然后用属性ID和产品ID去查询，看看这个属性和这个产品是否存在属性值了。
 * 如果不存在就创建一个属性值，并设置其属性和产品 插入到数据库
 * 完成初始化
 */
@Service
@CacheConfig(cacheNames = "propertyValues")
public class PropertyValueService {
    @Resource
    PropertyValueDAO propertyValueDAO;
    @Resource
    PropertyService propertyService;

    @CacheEvict(allEntries=true)
    public void update(PropertyValue bean) {
        propertyValueDAO.save(bean);
    }

    public void init(Product product){
        PropertyValueService propertyValueService = SpringContextUtil.getBean(PropertyValueService.class);
        List<Property> properties = propertyService.listByCategory(product.getCategory());
        for (Property property: properties){
            PropertyValue propertyValue = getByPropertyAndProduct(product, property);
            if (propertyValue == null) {
                propertyValue = new PropertyValue();
                propertyValue.setProduct(product);
                propertyValue.setProperty(property);
                propertyValueDAO.save(propertyValue);
            }
        }
    }

    @Cacheable(key="'propertyValues-one-pid-'+#p0.id+ '-ptid-' + #p1.id")
    public PropertyValue getByPropertyAndProduct(Product product, Property property){
        return propertyValueDAO.getByPropertyAndProduct(property, product);
    }

    @Cacheable(key="'propertyValues-pid-'+ #p0.id")
    public List<PropertyValue> list(Product product){
        return propertyValueDAO.findByProductOrderByIdDesc(product);
    }
}
