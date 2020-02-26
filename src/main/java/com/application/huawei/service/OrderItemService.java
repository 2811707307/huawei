package com.application.huawei.service;

import com.application.huawei.dao.OrderItemDAO;
import com.application.huawei.pojo.Order;
import com.application.huawei.pojo.OrderItem;
import com.application.huawei.pojo.Product;
import com.application.huawei.pojo.User;
import com.application.huawei.util.SpringContextUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @Auther: 10199
 * @Date: 2019/10/30 16:02
 * @Description: OrderItemService，提供对OrderItem的业务操作，其中主要是 fill 方法。
 * 从数据库中取出来的 Order 是没有 OrderItem集合的，这里通过 OrderItemDAO 取出来再放在 Order的 orderItems属性上。
 * 除此之外，还计算订单总数，总金额等等信息。
 */
@Service
@CacheConfig(cacheNames = "orderItems")
public class OrderItemService {
    @Resource
    OrderItemDAO orderItemDAO;
    @Resource
    ProductImageService productImageService;

    public void fill(List<Order> orders) {
        for (Order order : orders)
            fill(order);
    }


    public void fill(Order order) {
        //触发aop
        OrderItemService orderItemService = SpringContextUtil.getBean(OrderItemService.class);
        List<OrderItem> orderItems = listByOrder(order);
        float total = 0;
        int totalNumber = 0;
        for (OrderItem oi :orderItems) {
            total+=oi.getNumber()*oi.getProduct().getPromotePrice();
            totalNumber+=oi.getNumber();
            productImageService.setFirstProductImage(oi.getProduct());
        }
        order.setTotal(total);
        order.setOrderItems(orderItems);
        order.setTotalNumber(totalNumber);
    }

    @CacheEvict(allEntries = true)
    public void update(OrderItem orderItem) {
        orderItemDAO.save(orderItem);
    }

    @CacheEvict(allEntries = true)
    public void add(OrderItem orderItem){
        orderItemDAO.save(orderItem);
    }

    @Cacheable(key = "'orderItems-one-' + #p0")
    public OrderItem get(int id) {
        Optional<OrderItem> o = orderItemDAO.findById(id);
        OrderItem i = null;
        if (o.isPresent()){
            i = o.get();
        }
        return i;
    }

    @CacheEvict(allEntries = true)
    public void delete(int id) {
        orderItemDAO.deleteById(id);
    }

    public int getSaleCount(Product product){
        List<OrderItem> orderItems = listByProduct(product);
        int result = 0;
        for (OrderItem orderItem: orderItems){
            if (orderItem.getOrder() != null){
                if (orderItem.getOrder() != null && orderItem.getOrder().getPayDate() != null){
                    result = result + orderItem.getNumber();
                }
            }
        }
        return result;
    }

    @Cacheable(key = "'orderItems-pid-' + #p0.id")
    public List<OrderItem> listByProduct(Product product) {
        return orderItemDAO.findByProduct(product);
    }

    @Cacheable(key = "'orderItems-oid' + #p0.id")
    public List<OrderItem> listByOrder(Order order) {
        return orderItemDAO.findByOrderOrderByIdDesc(order);
    }

    @Cacheable(key = "'orderItems-uid' + #p0.id")
    public List<OrderItem> listByUser(User user){
        return orderItemDAO.findByUserAndOrderIsNull(user);
    }
}
