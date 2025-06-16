package com.edu.bcu.repository;

import com.edu.bcu.entity.FileRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRecordRepository extends JpaRepository<FileRecord, Long> {
    
    List<FileRecord> findByCategory(String category);
    
    List<FileRecord> findByRelatedId(Long relatedId);
    
    List<FileRecord> findByCategoryAndRelatedId(String category, Long relatedId);
    
    Optional<FileRecord> findByFileName(String fileName);
    
    @Query("SELECT f FROM FileRecord f WHERE f.status = 1 AND f.category = :category")
    List<FileRecord> findActiveByCategoryQuery(@Param("category") String category);
    
    @Query("SELECT f FROM FileRecord f WHERE f.status = 1 AND f.relatedId = :relatedId")
    List<FileRecord> findActiveByRelatedIdQuery(@Param("relatedId") Long relatedId);
} 