package com.malgn.allpics.domain.image.repository;

import com.malgn.allpics.domain.image.entity.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface ImageRepository extends JpaRepository<Image, String> {

    @Override
    Page<Image> findAll(Pageable pageable);

    Page<Image> findByTitleContaining(String keyword, Pageable pageable);

    @Query("SELECT i FROM Image i " +
            "WHERE (:title is null or i.title like %:title%) " +
            "AND (:categoryIds is null or i.category.categoryNum in :categoryIds) " +
            "AND (:tagNames is null or exists (" +
            "    SELECT t FROM ImageTag t " +
            "    WHERE t.image = i AND t.tagName in :tagNames" +
            "))")
    Page<Image> searchImages(@Param("title") String title,
                             @Param("categoryIds") List<Long> categoryIds,
                             @Param("tagNames") List<String> tagNames,
                             Pageable pageable);

}
