package com.application.huawei.controller.app;

import com.application.huawei.pojo.Order;
import com.application.huawei.pojo.OrderItem;
import com.application.huawei.pojo.Product;
import com.application.huawei.pojo.User;
import com.application.huawei.service.OrderItemService;
import com.application.huawei.service.OrderService;
import com.application.huawei.service.ProductImageService;
import com.application.huawei.service.ProductService;
import com.application.huawei.util.ResultUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 前台系统购买相关接口
 *
 * @Author: hpj
 * @Date: 2020/3/30 11:59
 */
@RestController
public class AppBuyController {
    @Resource
    ProductService productService;

    @Resource
    ProductImageService productImageService;

    @Resource
    OrderItemService orderItemService;

    @GetMapping("/frontBuyOne")
    public Object buyOne(int pid, int num, HttpSession session) {
        return buyOneAndAddCart(pid, num, session);
    }

    private int buyOneAndAddCart(int pid, int num, HttpSession session) {
        Product product = productService.get(pid);
        int oiid = 0;
        User user = (User) session.getAttribute("user");
        boolean found = false;
        List<OrderItem> orderItems = orderItemService.listByUser(user);

        for (OrderItem orderitem : orderItems) {
            if (orderitem.getProduct().getId() == product.getId()) {
                orderitem.setNumber(orderitem.getNumber() + num);
                orderItemService.update(orderitem);
                found = true;
                oiid = orderitem.getId();
                break;
            }
        }

        if (!found) {
            OrderItem orderItem = new OrderItem();
            orderItem.setUser(user);
            orderItem.setProduct(product);
            orderItem.setNumber(num);
            orderItemService.add(orderItem);
            oiid = orderItem.getId();
        }
        return oiid;
    }

    @GetMapping("/frontBuy")
    public Object buy(String[] oiid, HttpSession session) {
        List<OrderItem> orderItems = new ArrayList<>();
        float total = 0;

        for (String strid : oiid) {
            int id = Integer.parseInt(strid);
            OrderItem orderItem = orderItemService.get(id);
            total = total + orderItem.getProduct().getPromotePrice() * orderItem.getNumber();
            orderItems.add(orderItem);
        }

        productImageService.setFirstProductImagesOnOrderItems(orderItems);

        session.setAttribute("orderItems", orderItems);

        Map<String, Object> map = new HashMap<>();
        map.put("orderItems", orderItems);
        map.put("total", total);
        return ResultUtil.success(map);
    }

    @GetMapping("/frontAddCart")
    public Object addCart(int pid, int num, HttpSession session){
        buyOneAndAddCart(pid, num, session);
        return ResultUtil.success();
    }

    @GetMapping("/frontCart")
    public Object cart(HttpSession session){
        User user = (User) session.getAttribute("user");
        List<OrderItem> orderItems = orderItemService.listByUser(user);
        productImageService.setFirstProductImagesOnOrderItems(orderItems);
        return orderItems;
    }



}
