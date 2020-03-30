package com.application.huawei.controller.app;

import com.application.huawei.pojo.Order;
import com.application.huawei.pojo.Product;
import com.application.huawei.pojo.Review;
import com.application.huawei.pojo.User;
import com.application.huawei.service.OrderItemService;
import com.application.huawei.service.OrderService;
import com.application.huawei.service.ProductService;
import com.application.huawei.service.ReviewService;
import com.application.huawei.util.ResultUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: hpj
 * @Date: 2020/3/30 12:04
 */
@Resource
public class AppReviewController {

    @Resource
    OrderService orderService;

    @Resource
    OrderItemService orderItemService;

    @Resource
    ProductService productService;

    @Resource
    ReviewService reviewService;

    @GetMapping("/frontReview")
    public Object frontReview(int oid){
        Order o= orderService.get(oid);
        orderItemService.fill(o);
        orderService.removeOrderFromOrderItem(o);
        Product p = o.getOrderItems().get(0).getProduct();
        List<Review> reviews = reviewService.list(p);
        productService.setSaleAndReviewNumber(p);
        Map<String, Object> map = new HashMap<>();
        map.put("p", p);
        map.put("o", o);
        map.put("reviews", reviews);

        return  ResultUtil.success(map);
    }

    @PostMapping("/frontDoReview")
    public Object doReview(HttpSession session, int oid, int pid, String content){
        Order o = orderService.get(oid);
        o.setStatus(OrderService.finish);
        orderService.update(o);

        Product p = productService.get(pid);
        content = HtmlUtils.htmlEscape(content);

        User user = (User) session.getAttribute("user");
        Review review = new Review();
        review.setProduct(p);
        review.setContent(content);
        review.setCreateDate(new Date());
        review.setUser(user);
        reviewService.add(review);

        return ResultUtil.success();
    }
}
