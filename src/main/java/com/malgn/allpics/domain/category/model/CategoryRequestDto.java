package com.malgn.allpics.domain.category.model;

import com.malgn.allpics.domain.category.entity.Category;

import java.time.LocalDateTime;

public record CategoryRequestDto(
    Long categoryNum,
    String categoryName,
    int categoryOrder,
    LocalDateTime createdDate,
    LocalDateTime lastModifiedDate,
    LocalDateTime deletedDate,
    Long parentCategoryNum
    ) {

        public CategoryRequestDto(Category category){
            this(
                    category.getCategoryNum(),
                    category.getCategoryName(),
                    category.getCategoryOrder(),
                    category.getCreatedDate(),
                    category.getLastModifiedDate(),
                    category.getDeletedDate(),
                    category.getParentCategory() == null ? 0 : category.getParentCategory().getCategoryNum()
            );
        }

        public Category toEntity(){
            return Category.builder()
                    .categoryName(this.categoryName)
                    .categoryOrder(this.categoryOrder)
                    .build();
        }
    }
