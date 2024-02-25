package com.malgn.allpics.domain.category.model;

import com.malgn.allpics.domain.category.entity.Category;

import java.time.LocalDateTime;
import java.util.List;

public record CategoryResponseDto(
    Long categoryNum,
    String categoryName,
    int categoryOrder,
    LocalDateTime createdDate,
    LocalDateTime lastModifiedDate,
    LocalDateTime deletedDate,
    Long parentCategoryNum,
    List<Category> subCategoryList
    ) {
        public CategoryResponseDto(Category category){
            this(
                    category.getCategoryNum(),
                    category.getCategoryName(),
                    category.getCategoryOrder(),
                    category.getCreatedDate(),
                    category.getLastModifiedDate(),
                    category.getDeletedDate(),
                    category.getParentCategory() == null ? 0 : category.getParentCategory().getCategoryNum(),
                    category.getSubCategoryList()
            );
        }

        public Category toEntity(){
            return Category.builder()
                    .categoryName(this.categoryName)
                    .categoryOrder(this.categoryOrder)
                    .build();
        }
    }
