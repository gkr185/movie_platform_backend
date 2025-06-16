package com.bcu.movie.repository;

import com.bcu.movie.entity.Feedback;
import com.bcu.movie.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

// 反馈仓库接口
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    
    // 根据状态分页查询反馈，使用JOIN FETCH避免懒加载问题
    @Query("SELECT f FROM Feedback f JOIN FETCH f.user WHERE f.status = :status")
    Page<Feedback> findByStatus(Integer status, Pageable pageable);
    
    // 分页查询所有反馈，使用JOIN FETCH避免懒加载问题
    @Query("SELECT f FROM Feedback f JOIN FETCH f.user")
    Page<Feedback> findAllWithUser(Pageable pageable);
    
    // 根据用户查询反馈
    List<Feedback> findByUser(User user);
    
    // 根据用户ID查询反馈
    List<Feedback> findByUser_Id(Integer userId);
    
    // 根据反馈类型查询反馈
    List<Feedback> findByType(Integer type);
    
    // 根据状态查询反馈
    List<Feedback> findByStatus(Integer status);
}