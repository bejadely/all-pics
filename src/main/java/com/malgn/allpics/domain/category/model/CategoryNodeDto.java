package com.malgn.allpics.domain.category.model;

import com.malgn.allpics.domain.category.entity.Category;

public record CategoryNodeDto(String name,
                              Long id,
                              boolean load_on_demand) {
    public CategoryNodeDto(Category category){
        this(
                category.getCategoryName(),
                category.getCategoryNum(),
                true
        );
    }

}
