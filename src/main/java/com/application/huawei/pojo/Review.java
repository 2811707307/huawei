package com.application.huawei.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @Auther: 10199
 * @Date: 2019/11/14 19:30
 * @Description:
 */

@Data
@Entity
@Table(name = "review")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer"})
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name="uid")
    private  User user;

    @ManyToOne
    @JoinColumn(name = "pid")
    private Product product;

    private String content;
    private Date createDate;
}
