package com.application.huawei.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

/**
 * @Auther: 10199
 * @Date: 2019/10/29 21:49
 * @Description:
 */

@Data
@Entity
@Table(name="user")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    private String name;
    private String password;
    private String salt;

    @Transient
    private String anonymousName;
}
