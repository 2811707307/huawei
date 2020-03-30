package com.application.huawei.controller.app;

import com.application.huawei.pojo.Order;
import com.application.huawei.pojo.OrderItem;
import com.application.huawei.pojo.User;
import com.application.huawei.service.OrderItemService;
import com.application.huawei.service.OrderService;
import com.application.huawei.util.ResultUtil;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: hpj
 * @Date: 2020/3/30 12:02
 */
@RestController
public class AppOrderController {
    @Resource
    OrderService orderService;

    @Resource
    OrderItemService orderItemService;

    @GetMapping("/frontDeleteOrderItem")
    public Object deleteOrderItem(HttpSession session, int oiid) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResultUtil.fail("未登录");
        }
        orderItemService.delete(oiid);
        return ResultUtil.success();
    }

    @GetMapping("/frontChangeOrderItem")
    public Object changeOrderItem(HttpSession session, int pid, int num){
        User user = (User) session.getAttribute("user");
        if(user == null) {
            return ResultUtil.fail("未登录");
        }

        List<OrderItem> orderItems = orderItemService.listByUser(user);
        for (OrderItem orderItem : orderItems
        ) {
            if (orderItem.getProduct().getId() == pid){
                orderItem.setNumber(num);
                orderItemService.update(orderItem);
                break;
            }
        }
        return ResultUtil.success();
    }

    @PostMapping("/frontCreateOrder")
    public Object createOrder(@RequestBody Order order, HttpSession session){
        User user =(User)  session.getAttribute("user");
        if(null==user) {
            return ResultUtil.fail("未登录");
        }
        String orderCode = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + RandomUtils.nextInt(10000);
        order.setOrderCode(orderCode);
        order.setCreateDate(new Date());
        order.setUser(user);
        order.setStatus(OrderService.waitPay);
        List<OrderItem> ois= (List<OrderItem>)  session.getAttribute("orderItems");

        float total =orderService.add(order,ois);

        Map<String,Object> map = new HashMap<>();
        map.put("oid", order.getId());
        map.put("total", total);

        return ResultUtil.success(map);
    }

    @GetMapping("/frontBought")
    public Object bought(HttpSession session) {
        User user =(User) session.getAttribute("user");
        if (user == null) {
            return ResultUtil.fail("未登录");
        }

        List<Order> orders = orderService.listByUserWithoutDelete(user);
        orderService.removeOrderFromOrderItem(orders);

        return orders;
    }


    @GetMapping("/frontDeleteOrder")
    public Object deleteOrder(int oid){
        Order order = orderService.get(oid);
        order.setStatus(OrderService.delete);
        orderService.update(order);
        return ResultUtil.success();
    }

    @GetMapping("/frontOrderConfirmed")
    public Object orderConfirmed(int oid){
        Order o = orderService.get(oid);
        o.setStatus(OrderService.waitReview);
        o.setConfirmDate(new Date());
        orderService.update(o);
        return ResultUtil.success();
    }

    @GetMapping("/frontPayed")
    public Object payed(int oid) {
        Order order = orderService.get(oid);
        order.setStatus(OrderService.waitDelivery);
        order.setPayDate(new Date());
        orderService.update(order);
        return order;
    }

}
