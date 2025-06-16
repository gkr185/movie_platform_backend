package com.bcu.edu.service.impl;

import com.bcu.edu.entity.Advertisement;
import com.bcu.edu.repository.AdvertisementRepository;
import com.bcu.edu.service.AdvertisementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class AdvertisementServiceImpl implements AdvertisementService {

    @Autowired
    private AdvertisementRepository advertisementRepository;

    @Override
    @Transactional
    public Advertisement createAdvertisement(Advertisement advertisement) {
        advertisement.setCreateTime(LocalDateTime.now());
        advertisement.setUpdateTime(LocalDateTime.now());
        if (advertisement.getStatus() == null) {
            advertisement.setStatus(0); // 默认未生效
        }
        if (advertisement.getSortOrder() == null) {
            advertisement.setSortOrder(0);
        }
        return advertisementRepository.save(advertisement);
    }

    @Override
    @Transactional
    public Advertisement updateAdvertisement(Integer id, Advertisement advertisement) {
        Advertisement existingAd = advertisementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("广告不存在"));
        
        existingAd.setTitle(advertisement.getTitle());
        existingAd.setImageUrl(advertisement.getImageUrl());
        existingAd.setVideoUrl(advertisement.getVideoUrl());
        existingAd.setLinkUrl(advertisement.getLinkUrl());
        existingAd.setPosition(advertisement.getPosition());
        existingAd.setSortOrder(advertisement.getSortOrder());
        existingAd.setStartTime(advertisement.getStartTime());
        existingAd.setEndTime(advertisement.getEndTime());
        existingAd.setStatus(advertisement.getStatus());
        existingAd.setUpdateTime(LocalDateTime.now());
        
        return advertisementRepository.save(existingAd);
    }

    @Override
    @Transactional
    public void deleteAdvertisement(Integer id) {
        Advertisement advertisement = advertisementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("广告不存在"));
        advertisementRepository.delete(advertisement);
    }

    @Override
    public Page<Advertisement> getAllAdvertisements(Pageable pageable) {
        return advertisementRepository.findAll(pageable);
    }

    @Override
    public List<Advertisement> getAllImageAdvertisements() {
        return advertisementRepository.findAllImageAds();
    }

    @Override
    public Advertisement getRandomVideoAdvertisement() {
        List<Advertisement> videoAds = advertisementRepository.findAllVideoAds();
        if (videoAds.isEmpty()) {
            return null;
        }
        Random random = new Random();
        return videoAds.get(random.nextInt(videoAds.size()));
    }

    @Override
    public Advertisement getAdvertisementById(Integer id) {
        return advertisementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("广告不存在，ID: " + id));
    }
} 