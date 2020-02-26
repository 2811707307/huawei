package com.application.huawei.dao;

import com.application.huawei.pojo.Product;
import com.application.huawei.pojo.Property;
import com.application.huawei.pojo.PropertyValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Auther: 10199
 * @Date: 2019/10/28 14:45
 * @Description:
 */
public interface PropertyValueDAO extends JpaRepository<PropertyValue, Integer> {
    List<PropertyValue> findByProductOrderByIdDesc(Product product);
    PropertyValue getByPropertyAndProduct(Property property, Product product);
}
