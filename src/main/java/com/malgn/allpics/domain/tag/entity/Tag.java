package com.malgn.allpics.domain.tag.entity;

import com.malgn.allpics.common.entity.BaseTime;
import com.malgn.allpics.domain.imagetag.entity.ImageTag;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "tags")
public class Tag extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagNum;
    private String tagName;

    @OneToMany(mappedBy = "tag")
    @ToString.Exclude
    List<ImageTag> imageTagList = new ArrayList<>();

    @Builder
    private Tag (String tagName){
        this.tagName = tagName;
    }

}
