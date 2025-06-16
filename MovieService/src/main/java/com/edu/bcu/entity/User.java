package com.edu.bcu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "int unsigned")
    private Integer id;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "avatar", length = 255)
    private String avatar = "default_avatar.jpg";

    @Column(name = "register_time", insertable = false, updatable = false)
    private LocalDateTime registerTime;

    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;

    @Column(name = "user_status")
    private Integer userStatus;

    @Column(name = "is_vip")
    private Integer isVip;

    @Column(name = "vip_expire_time")
    private LocalDateTime vipExpireTime;

    @Column(name = "create_time", insertable = false, updatable = false)
    private LocalDateTime createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private LocalDateTime updateTime;
}