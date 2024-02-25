package com.malgn.allpics.domain.imagetag.repository;

import com.malgn.allpics.domain.image.entity.Image;
import com.malgn.allpics.domain.imagetag.entity.ImageTag;
import com.malgn.allpics.domain.imagetag.model.ImageTagSumDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageTagRepository extends JpaRepository<ImageTag, Long> {

    void deleteByTagNameAndImage(String tagName, Image image);

    @Query("SELECT new com.malgn.allpics.domain.imagetag.model.ImageTagSumDto( i.tagName, COUNT (i) ) FROM ImageTag i WHERE i.image.imageNum = :imageNum GROUP BY i.tagName")
    List<ImageTagSumDto> findByTagNameWithSumForImage(@Param("imageNum") String imageNum);

    @Query("SELECT new com.malgn.allpics.domain.imagetag.model.ImageTagSumDto( i.tagName, COUNT (i) ) FROM ImageTag i GROUP BY i.tagName")
    List<ImageTagSumDto> findAllTagNameWithSum();

    void deleteByImage(Image image);

}
