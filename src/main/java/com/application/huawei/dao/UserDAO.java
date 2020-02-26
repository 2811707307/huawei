package com.application.huawei.dao;

import com.application.huawei.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Auther: 10199
 * @Date: 2019/10/30 13:14
 * @Description:
 */
public interface UserDAO extends JpaRepository<User, Integer> {
    User findByName(String name);
}
