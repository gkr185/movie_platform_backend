package com.bcu.movie.repository;

import com.bcu.movie.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    
    /**
     * 更新用户VIP状态
     * @param userId 用户ID
     * @param isVip VIP状态（1:是VIP，0:不是VIP）
     * @param vipExpireTime VIP过期时间
     */
    @Modifying
    @Query("UPDATE User u SET u.isVip = :isVip, u.vipExpireTime = :vipExpireTime, u.updateTime = :updateTime WHERE u.id = :userId")
    void updateVipStatus(@Param("userId") Integer userId, 
                        @Param("isVip") Integer isVip, 
                        @Param("vipExpireTime") LocalDateTime vipExpireTime,
                        @Param("updateTime") LocalDateTime updateTime);
    
    /**
     * 取消用户VIP状态
     * @param userId 用户ID
     */
    @Modifying
    @Query("UPDATE User u SET u.isVip = 0, u.vipExpireTime = NULL, u.updateTime = :updateTime WHERE u.id = :userId")
    void cancelVipStatus(@Param("userId") Integer userId, @Param("updateTime") LocalDateTime updateTime);
} 