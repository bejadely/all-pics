package com.malgn.allpics.domain.image.entity;

import com.malgn.allpics.common.entity.BaseTime;
import com.malgn.allpics.domain.category.entity.Category;
import com.malgn.allpics.domain.imagetag.entity.ImageTag;
import com.malgn.allpics.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "images")
public class Image extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_num")
    private String imageNum;
    private String title;
    private String savePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_num")
    @ToString.Exclude
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_num")
    @ToString.Exclude
    private Category category;

    @OneToMany(mappedBy = "image", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    List<ImageTag> imageTagList = new ArrayList<>();

    @Builder
    private Image(String title, String savePath, Member member, Category category){

        this.title = title;
        this.savePath = savePath;
        this.member = member;
        this.category = category;

    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setSavePath(String savePath){ this.savePath = savePath; }

    public void setTitle(String title){ this.title = title; }

}