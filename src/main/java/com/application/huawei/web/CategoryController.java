package com.application.huawei.web;

import com.application.huawei.pojo.Category;
import com.application.huawei.service.CategoryService;
import com.application.huawei.util.ImageUtil;
import com.application.huawei.util.PageUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Rest规范Controller
 * 尚未解决图片缓存问题，解决思路：使用随机数地址，待测试
 */

@RestController
public class CategoryController {
    @Resource
    CategoryService categoryService;

    @GetMapping("/categories")
    public PageUtil<Category> list(@RequestParam(value = "start", defaultValue = "0") int start,
                                   @RequestParam(value = "size", defaultValue = "10") int size) throws Exception {
        //此处‘防止开始页为负数，如果出现负数的情况修正为0
        start = start<0?0:start;
        //8表示导航分页最多有8个.
        PageUtil<Category> page =categoryService.list(start, size, 8);
        return page;
    }

    @PostMapping("/categories")
    public Object add(Category bean, MultipartFile image, HttpServletRequest request) throws Exception {
        categoryService.add(bean);
        saveOrUpdateImageFile(bean, image, request);
        return bean;
    }

    //categoryService根据Category类型删除，@click传递过来的参数为bean的id
    @DeleteMapping("/categories/{id}")
    public String delete(@PathVariable("id") Category bean) throws Exception{
        categoryService.delete(bean);
        File imageFolder = new File(System.getProperty("user.home") + "/huawei/img/category");
        File file = new File(imageFolder, bean.getId()+".jpg");
        file.delete();
        return null;
    }

    @GetMapping("/categories/{id}")
    public Category get(@PathVariable("id") int id) throws Exception{
        return categoryService.get(id);
    }

    //增加update函数，它是用 PutMapping 来映射的，因为 REST要求修改用PUT来做。
    @PutMapping("/categories/{id}")
    public Object update(Category bean, MultipartFile image, HttpServletRequest request) throws Exception{
        categoryService.update(bean);
        if (image != null) {
            saveOrUpdateImageFile(bean, image, request);
        }
        return bean;
    }

    public void saveOrUpdateImageFile(Category bean, MultipartFile image, HttpServletRequest request)
            throws IOException {
        //获取项目打包的实际目录
//        File imageFolder= new File(ResourceUtils.getURL("classpath:").getPath() + "img/category");
        //获取服务器发布后的目录，待笔记
//        File imageFolder= new File(request.getServletContext().getRealPath("img/category"));
        String home = System.getProperty("user.home");
        File imageFolder = new File(  home + "/huawei/img/category");
        File file = new File(imageFolder,bean.getId()+".jpg");
        if(!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        image.transferTo(file);
        BufferedImage img = ImageUtil.change2jpg(file);
        ImageIO.write(img, "jpg", file);
    }

}

