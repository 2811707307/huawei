package com.application.huawei.controller.admin;

import com.application.huawei.pojo.Product;
import com.application.huawei.pojo.ProductImage;
import com.application.huawei.service.CategoryService;
import com.application.huawei.service.ProductImageService;
import com.application.huawei.service.ProductService;
import com.application.huawei.util.ImageUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 产品图片管理 API
 *
 * @Auther: 10199
 * @Date: 2019/10/24 20:34
 * @Description:
 */

@RestController
public class ProductImageController {
    private static  final String home = System.getProperty("user.home");
    @Resource
    ProductService productService;
    @Resource
    ProductImageService productImageService;
    @Resource
    CategoryService categoryService;

    @GetMapping("/products/{pid}/productImages")
    public List<ProductImage> list(@PathVariable("pid") int pid, @RequestParam("type") String type) throws Exception{
        Product product = productService.get(pid);
        if (ProductImageService.type_single.equals(type)) {
            List<ProductImage> singles = productImageService.listSingleProductImages(product);
            return singles;
        }else if (ProductImageService.type_detail.equals(type)){
            List<ProductImage> details = productImageService.listDetailProductImages(product);
            return details;
        }else{
            return new ArrayList<>();
        }
    }

    @PostMapping("/productImages")
    public Object add(@RequestParam("pid") int pid, @RequestParam("type") String type, HttpServletRequest request, MultipartFile image) {
        ProductImage bean = new ProductImage();
        Product product = productService.get(pid);
        bean.setProduct(product);
        bean.setType(type);

        productImageService.add(bean);
        String folder = "/huawei/img/";
//        String home = System.getProperty("user.home");
        //处理展示图片跟详情页图片
        if (ProductImageService.type_single.equals(bean.getType()))
            folder = folder + "productSingle";
        else
            folder = folder + "productDetail";

        File imageFolder = new File(home + folder);
        File imageFile = new File(imageFolder, bean.getId() + ".jpg");
        String fileName = imageFile.getName();

        if(!imageFile.getParentFile().exists())
            imageFile.getParentFile().mkdirs();

        try {
            image.transferTo(imageFile);
            BufferedImage img = ImageUtil.change2jpg(imageFile);
            ImageIO.write(img, "jpg", imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //处理展示图片的几种大小类型
        if (ProductImageService.type_single.equals(bean.getType())) {
            String imageFolder_small = home + "/huawei/img/productSingle_small";
            String imageFolder_middle = home + "/huawei/img/productDetail_middle";

            File i_small = new File(imageFolder_small, fileName);
            File i_middle = new File(imageFolder_middle, fileName);
            i_small.getParentFile().mkdirs();
            i_middle.getParentFile().mkdirs();
            ImageUtil.resizeImage(imageFile, 56, 56, i_small);
            ImageUtil.resizeImage(imageFile, 217, 190, i_middle);
        }
        return bean;
    }

    @DeleteMapping("/productImages/{id}")
    public String delete(@PathVariable("id") int id) throws Exception{
        //删除数据库对象
        ProductImage bean = productImageService.get(id);
        productImageService.delete(bean);

        //删除磁盘文件
        String folder = "/huawei/img";
        if (ProductImageService.type_single.equals(bean.getType()))
            folder = folder + "productSingle";
        else
            folder = folder + "productDetail";

        File imageFolder = new File(home + folder);
        File imageFile = new File(imageFolder, bean.getId() + ".jpg");
        String fileName = imageFile.getName();
        imageFile.delete();

        if (ProductImageService.type_single.equals(bean.getType())) {
            String imageFolder_small = home + "/huawei/img/productSingle_small";
            String imageFolder_middle = home + "/huawei/img/productDetail_middle";

            File i_small = new File(imageFolder_small, fileName);
            File i_middle = new File(imageFolder_middle, fileName);
            i_small.delete();
            i_middle.delete();
        }
        return null;
    }
}
