package com.application.huawei.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

/**
 * @Auther: 10199
 * @Date: 2019/10/28 11:05
 * @Description: 产品属性值
 */
@Data
@Entity
@Table(name="propertyValue")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer"})
public class PropertyValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "pid")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "ptid")
    private Property property;

    private String value;
}
