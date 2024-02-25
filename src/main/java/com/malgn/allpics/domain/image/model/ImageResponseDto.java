package com.malgn.allpics.domain.image.model;

import com.malgn.allpics.domain.image.entity.Image;
import com.malgn.allpics.domain.imagetag.entity.ImageTag;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder(toBuilder = true)
public record ImageResponseDto(
    String imageNum,
    String title,
    String savePath,
    String memberId,
    Long categoryNum,
    LocalDateTime createdDate,
    LocalDateTime lastModifiedDate,
    List<ImageTag> tagList
) {
    public ImageResponseDto(Image image){
        this(
                image.getImageNum(),
                image.getTitle(),
                image.getSavePath(),
                image.getMember().getMemberId(),
                image.getCategory().getCategoryNum(),
                image.getCreatedDate(),
                image.getLastModifiedDate(),
                image.getImageTagList()
        );
    }
}
