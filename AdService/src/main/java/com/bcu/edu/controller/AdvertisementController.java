package com.bcu.edu.controller;

import com.bcu.edu.entity.Advertisement;
import com.bcu.edu.service.AdvertisementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/advertisements")
public class AdvertisementController {

    @Autowired
    private AdvertisementService advertisementService;

    // 创建广告
    @PostMapping
    public ResponseEntity<Advertisement> createAdvertisement(@RequestBody Advertisement advertisement) {
        try {
            Advertisement createdAd = advertisementService.createAdvertisement(advertisement);
            return ResponseEntity.ok(createdAd);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 更新广告
    @PutMapping("/{id}")
    public ResponseEntity<Advertisement> updateAdvertisement(@PathVariable Integer id, @RequestBody Advertisement advertisement) {
        try {
            Advertisement updatedAd = advertisementService.updateAdvertisement(id, advertisement);
            return ResponseEntity.ok(updatedAd);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 删除广告
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdvertisement(@PathVariable Integer id) {
        try {
            advertisementService.deleteAdvertisement(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 获取广告列表（分页）
    @GetMapping
    public ResponseEntity<Page<Advertisement>> getAllAdvertisements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createTime") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        
        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        Page<Advertisement> advertisements = advertisementService.getAllAdvertisements(pageRequest);
        return ResponseEntity.ok(advertisements);
    }

    // 获取所有图片广告
    @GetMapping("/images")
    public ResponseEntity<List<Advertisement>> getAllImageAdvertisements() {
        List<Advertisement> imageAds = advertisementService.getAllImageAdvertisements();
        return ResponseEntity.ok(imageAds);
    }

    // 随机获取一个视频广告
    @GetMapping("/video/random")
    public ResponseEntity<Advertisement> getRandomVideoAdvertisement() {
        Advertisement videoAd = advertisementService.getRandomVideoAdvertisement();
        if (videoAd != null) {
            return ResponseEntity.ok(videoAd);
        }
        return ResponseEntity.notFound().build();
    }
} 