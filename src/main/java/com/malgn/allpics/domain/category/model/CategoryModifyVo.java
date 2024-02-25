package com.malgn.allpics.domain.category.model;

public record CategoryModifyVo (
        Long movedCategoryId,
        Long targetCategoryId,
        String movedPosition
){
}
