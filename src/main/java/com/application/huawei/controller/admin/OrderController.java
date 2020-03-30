package com.application.huawei.controller.admin;

import com.application.huawei.pojo.Order;
import com.application.huawei.service.OrderItemService;
import com.application.huawei.service.OrderService;
import com.application.huawei.util.PageUtil;
import com.application.huawei.util.ResultUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 后台订单管理API
 *
 * @Auther: 10199
 * @Date: 2019/10/30 18:53
 * @Description:
 */
@RestController
public class OrderController {
    @Resource
    OrderService orderService;
    @Resource
    OrderItemService orderItemService;

    @GetMapping("/orders")
    public PageUtil<Order> list(@RequestParam(value = "start", defaultValue = "0")int start, @RequestParam(value = "size", defaultValue = "5")int size) throws Exception{
        start = start < 0?0:start;
        PageUtil<Order> page = orderService.list(start, size, 5);
        orderItemService.fill(page.getContent());
        orderService.removeOrderFromOrderItem(page.getContent());
        return page;
    }

    @PutMapping("deliveryOrder/{oid}")
    public Object deliveryOrder(@PathVariable int oid){
        Order o = orderService.get(oid);
        o.setDeliveryDate(new Date());
        o.setStatus(OrderService.waitConfirm);
        orderService.update(o);
        return ResultUtil.success();
    }
}
