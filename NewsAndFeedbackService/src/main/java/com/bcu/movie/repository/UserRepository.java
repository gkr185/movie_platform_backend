package com.bcu.movie.repository;

import com.bcu.movie.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

// 用户仓库接口
public interface UserRepository extends JpaRepository<User, Integer> {
}