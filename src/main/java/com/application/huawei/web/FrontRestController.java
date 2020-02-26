package com.application.huawei.web;

import com.application.huawei.pojo.*;
import com.application.huawei.service.*;
import com.application.huawei.util.ResultUtil;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Auther: 10199
 * @Date: 2019/11/8 15:28
 * @Description:
 */
@RestController
public class FrontRestController {
    @Resource
    ProductService productService;
    @Resource
    UserService userService;
    @Resource
    ProductImageService productImageService;
    @Resource
    PropertyValueService propertyValueService;
    @Resource
    ReviewService reviewService;
    @Resource
    OrderItemService orderItemService;
    @Resource
    OrderService orderService;


    @PostMapping("/frontRegister")
    public Object register(@RequestBody User user) {
        String name = user.getName();
        String password = user.getPassword();
        //转义特殊字符
        name = HtmlUtils.htmlEscape(name);
        user.setName(name);
        boolean exist = userService.isExist(name);

        if (exist) {
            String message = "用户名已经被使用,不能使用";
            return ResultUtil.fail(message);
        }

        //使用Shiro 盐加密
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;
        String algorithmName = "md5";
        String encodedPassword = new SimpleHash(algorithmName, password, salt, times).toString();

        user.setSalt(salt);
        user.setPassword(encodedPassword);
        userService.add(user);
        return ResultUtil.success();
    }

    @PostMapping("/frontLogin")
    public Object login(@RequestBody User userParam, HttpSession session) {
        String name = userParam.getName();
        name = HtmlUtils.htmlEscape(name);

        //登录时使用Shiro的方式进行校验
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(name, userParam.getPassword());
        try {
            subject.login(token);
            User user =  userService.getByName(name);
            subject.getSession().setAttribute("user", user);
            return ResultUtil.success();
        }catch (AuthenticationException e){
            String message = "账号或密码错误";
            return ResultUtil.fail(message);
        }
    }

    @GetMapping("/frontProduct/{pid}")
    public Object product(@PathVariable("pid") int pid) {
        Product product = productService.get(pid);

        List<ProductImage> productSingleImages = productImageService.listSingleProductImages(product);
        List<ProductImage> productDetailImages = productImageService.listDetailProductImages(product);
        product.setProductSingleImages(productSingleImages);
        product.setProductDetailImages(productDetailImages);

        List<PropertyValue> pvs = propertyValueService.list(product);
        List<Review> reviews = reviewService.list(product);
        productService.setSaleAndReviewNumber(product);
        productImageService.setFirstProductImage(product);

        Map<String, Object> map = new HashMap<>();
        map.put("product", product);
        map.put("pvs", pvs);
        map.put("reviews", reviews);

        return ResultUtil.success(map);
    }

    @GetMapping("/frontCheckLogin")
    public Object checkLogin(HttpSession session) {
        Subject subject = SecurityUtils.getSubject();
        if(subject.isAuthenticated())
            return ResultUtil.success();
        else
            return ResultUtil.fail("未登录");
    }

    @PostMapping("/frontSearch")
    public Object search(String keyword) {
        if (keyword == null)
            keyword = "";
        List<Product> products = productService.search(keyword, 0, 20);
        productImageService.setFirstProductImages(products);
        productService.setSaleAndReviewNumber(products);
        return products;
    }

    /**
     * @param:
     * @return:
     * @auther: 10199
     * @date: 2019/11/19 16:39
     * @Description: 1. 获取参数pid
     * 2. 获取参数num
     * 3. 根据pid获取产品对象p
     * 4. 从session中获取用户对象user
     * <p>
     * 接下来就是新增订单项OrderItem， 新增订单项要考虑两个情况
     * a. 如果已经存在这个产品对应的OrderItem，并且还没有生成订单，即还在购物车中。 那么就应该在对应的OrderItem基础上，调整数量
     * a.1 基于用户对象user，查询没有生成订单的订单项集合
     * a.2 遍历这个集合
     * a.3 如果产品是一样的话，就进行数量追加
     * a.4 获取这个订单项的 id
     * <p>
     * b. 如果不存在对应的OrderItem,那么就新增一个订单项OrderItem
     * b.1 生成新的订单项
     * b.2 设置数量，用户和产品
     * b.3 插入到数据库
     * b.4 获取这个订单项的 id
     * <p>
     * 5.返回当前订单项id
     * 6. 在页面上，拿到这个订单项id，就跳转到 location.href="buy?oiid="+oiid;
     * buy 是结算页面，现在还没有做，在下一个知识点就会做了。
     * <p>
     * 因为增加到购物车的逻辑和这个是一样的，所以都重构到 buyoneAndAddCart 方法里了，方便后续增加购物车行为。
     */
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

    @GetMapping("/frontDeleteOrderItem")
    public Object deleteOrderItem(HttpSession session, int oiid) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return ResultUtil.fail("未登录");
        orderItemService.delete(oiid);
        return ResultUtil.success();
    }

    @GetMapping("/frontChangeOrderItem")
    public Object changeOrderItem(HttpSession session, int pid, int num){
        User user = (User) session.getAttribute("user");
        if(user == null)
            return ResultUtil.fail("未登录");

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
    public Object createOrder(@RequestBody Order order,HttpSession session){
        User user =(User)  session.getAttribute("user");
        if(null==user)
            return ResultUtil.fail("未登录");
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

    @GetMapping("/frontPayed")
    public Object payed(int oid) {
        Order order = orderService.get(oid);
        order.setStatus(OrderService.waitDelivery);
        order.setPayDate(new Date());
        orderService.update(order);
        return order;
    }

    @GetMapping("/frontBought")
    public Object bought(HttpSession session) {
        User user =(User) session.getAttribute("user");
        if (user == null)
            return ResultUtil.fail("未登录");

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
