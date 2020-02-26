package com.application.huawei.service;

import com.application.huawei.dao.UserDAO;
import com.application.huawei.pojo.User;
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

/**
 * @Auther: 10199
 * @Date: 2019/10/30 13:15
 * @Description:
 */

@Service
@CacheConfig(cacheNames = "users")
public class UserService {
    @Resource
    UserDAO userDAO;

    public boolean isExist(String name) {
        User user = getByName(name);
        return null!=user;
    }

    @Cacheable(key="'users-one-name-'+ #p0")
    public User getByName(String name) {
        return userDAO.findByName(name);
    }

    @CacheEvict(allEntries=true)
    public void add(User user) {
        userDAO.save(user);
    }

    @Cacheable(key="'users-page-'+#p0+ '-' + #p1")
    public PageUtil<User> list(int start, int size, int navigatePages) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(start, size, sort);
        Page pageFromJPA = userDAO.findAll(pageable);
        return new PageUtil<>(pageFromJPA, navigatePages);
    }

    public User findByNameAndPassword(String name, String password){

        return null;
    }
}
