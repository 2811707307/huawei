package com.application.huawei.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @Auther: 10199
 * @Date: 2019/10/23 16:31
 * @Description: 产品 实体类
 */

@Entity
@Table(name = "product")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer"})
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;

    @ManyToOne
    @JoinColumn(name="cid")
    private Category category;

    //如果既没有指明 关联到哪个Column,又没有明确要用@Transient忽略，那么就会自动关联到表对应的同名字段
    private String name;
    private String subTitle;
    private float originalPrice;
    private float promotePrice;
    private int stock;
    private Date createDate;
    @Transient
    private ProductImage firstProductImage;
    //单个产品图片集合
    @Transient
    private List<ProductImage> productSingleImages;
    //详情页产品集合
    @Transient
    private List<ProductImage> productDetailImages;
    //销量
    @Transient
    private int saleCount;
    //总计评论
    @Transient
    private int reviewCount;
}
