package com.application.huawei.service;

import com.application.huawei.dao.OrderDAO;
import com.application.huawei.pojo.Order;
import com.application.huawei.pojo.OrderItem;
import com.application.huawei.pojo.User;
import com.application.huawei.util.PageUtil;
import com.application.huawei.util.SpringContextUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @Auther: 10199
 * @Date: 2019/10/30 16:17
 * @Description: OrderService, 提供分页查询。
 * 还提供了 订单状态的常量，Order.java 的 getStatusDesc 会用到。
 * 另外还提供了一个奇怪的方法，removeOrderFromOrderItem，它的作用是把订单里的订单项的订单属性设置为空
 */
@Service
@CacheConfig(cacheNames = "orders")
public class OrderService {
    public static final String waitPay = "waitPay";
    public static final String waitDelivery = "waitDelivery";
    public static final String waitConfirm = "waitConfirm";
    public static final String waitReview = "waitReview";
    public static final String finish = "finish";
    public static final String delete = "delete";

    @Resource
    private OrderDAO orderDAO;

    @Resource
    private OrderService orderService;

    @Resource
    private OrderItemService orderItemService;

    @Cacheable(key = "'orders-page-' + #p0 + '-' + #p1")
    public PageUtil<Order> list(int start, int size, int navigatePages){
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(start, size, sort);
        Page pageFromJPA = orderDAO.findAll(pageable);
        return new PageUtil<>(pageFromJPA, navigatePages);
    }


    public void removeOrderFromOrderItem(List<Order> orders){
        for(Order order:orders)
            removeOrderFromOrderItem(order);
    }

    public void removeOrderFromOrderItem(Order order){
        List<OrderItem> orderItems = order.getOrderItems();
        for (OrderItem orderItem : orderItems)
            orderItem.setOrder(null);
    }

    @Cacheable(key = "'orders-one' + #p0")
    public Order get(int oid){
        Optional<Order> o = orderDAO.findById(oid);
        Order order = null;
        if(o.isPresent())
            order = o.get();
        return order;
    }

    @CacheEvict(allEntries=true)
    public void update(Order bean) {
        orderDAO.save(bean);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackForClassName = "Exception")
    @CacheEvict(allEntries=true)
    public float add(Order order, List<OrderItem> orderItems){
        float total = 0;
        add(order);

        for (OrderItem oi: orderItems) {
            oi.setOrder(order);
            orderItemService.update(oi);
            total += oi.getProduct().getPromotePrice()*oi.getNumber();
        }
        return total;
    }

    @CacheEvict(allEntries=true)
    public void add(Order order) {
        orderDAO.save(order);
    }

    public List<Order> listByUserWithoutDelete(User user){
        OrderService orderService = SpringContextUtil.getBean(OrderService.class);
        List<Order> orders = listByUserAndStatusNotOrderByIdDesc(user);
        orderItemService.fill(orders);
        return orders;
    }

    @Cacheable(key="'orders-uid-'+ #p0.id")
    public List<Order> listByUserAndStatusNotOrderByIdDesc(User user) {
        return orderDAO.findByUserAndStatusNotOrderByIdDesc(user, OrderService.delete);
    }

    public void cacl(Order o){
        List<OrderItem> orderItems = o.getOrderItems();
        float total = 0;
        for (OrderItem orderItem : orderItems
             ) {
            total += orderItem.getProduct().getPromotePrice()*orderItem.getNumber();
        }
        o.setTotal(total);
    }
}
