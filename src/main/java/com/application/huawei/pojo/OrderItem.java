package com.application.huawei.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

/**
 * @Auther: 10199
 * @Date: 2019/10/30 15:45
 * @Description:
 */
@Data
@Entity
@Table(name = "orderItem")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "pid")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "uid")
    private User user;

    @ManyToOne
    @JoinColumn(name = "oid")
    private Order order;

    private int number;
}
