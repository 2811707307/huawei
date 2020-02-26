package com.application.huawei.dao;

import com.application.huawei.pojo.Order;
import com.application.huawei.pojo.OrderItem;
import com.application.huawei.pojo.Product;
import com.application.huawei.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Auther: 10199
 * @Date: 2019/10/30 15:57
 * @Description:
 */
public interface OrderItemDAO extends JpaRepository<OrderItem, Integer> {
    List<OrderItem> findByOrderOrderByIdDesc(Order order);
    List<OrderItem> findByProduct(Product product);
    List<OrderItem> findByUserAndOrderIsNull(User user);
}
