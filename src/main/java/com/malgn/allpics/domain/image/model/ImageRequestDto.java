package com.malgn.allpics.domain.image.model;

import com.malgn.allpics.domain.image.entity.Image;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder(toBuilder = true)
public record ImageRequestDto(
        String imageNum,
        String title,
        String savePath,
        String memberId,
        Long categoryNum,
        MultipartFile file,
        String tag,
        List<String> tagNames
        ) {

    public Image toEntity(){
        return Image.builder()
                .title(this.title)
                .savePath(this.savePath)
                .build();
    }
}
