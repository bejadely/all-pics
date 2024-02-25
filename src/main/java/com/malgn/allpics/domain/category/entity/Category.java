package com.malgn.allpics.domain.category.entity;

import com.malgn.allpics.common.entity.BaseTime;
import com.malgn.allpics.domain.image.entity.Image;
import jakarta.persistence.*;
import lombok.*;
import lombok.ToString.Exclude;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "categories")
public class Category extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryNum;
    private String categoryName;
    private int categoryOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_num")
    @Exclude
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @Exclude
    private List<Category> subCategoryList = new ArrayList<>();

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @Exclude
    private List<Image> imageList;

    @Builder
    private Category(String categoryName, int categoryOrder) {

        this.categoryName = categoryName;
        this.categoryOrder = categoryOrder;
    }

    public void setParentCategory(Category parentCategory) { this.parentCategory = parentCategory; }

    public void setCategoryOrder(int categoryOrder){
        this.categoryOrder = categoryOrder;
    }

}
