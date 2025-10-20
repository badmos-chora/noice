package com.noice.media.repository;

import com.noice.media.entity.ImageAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageAssetRepository extends JpaRepository<ImageAsset, Long>, JpaSpecificationExecutor<ImageAsset> {
}