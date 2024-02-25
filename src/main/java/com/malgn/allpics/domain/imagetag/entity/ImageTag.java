package com.malgn.allpics.domain.imagetag.entity;

import com.malgn.allpics.domain.image.entity.Image;
import com.malgn.allpics.domain.tag.entity.Tag;
import jakarta.persistence.*;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "images_tags")
public class ImageTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageTagNum;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "image_num")
    @ToString.Exclude
    private Image image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_num")
    @ToString.Exclude
    private Tag tag;
    private String tagName;

    @Builder
    private ImageTag(Image image, Tag tag, String tagName){
        this.image = image;
        this.tag = tag;
        this.tagName = tagName;
    }

}
