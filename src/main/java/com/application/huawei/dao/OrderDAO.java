package com.application.huawei.dao;

import com.application.huawei.pojo.Order;
import com.application.huawei.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Auther: 10199
 * @Date: 2019/10/30 15:57
 * @Description:
 */

public interface OrderDAO extends JpaRepository<Order, Integer> {
    List<Order> findByUserAndStatusNotOrderByIdDesc(User user, String status);
}
