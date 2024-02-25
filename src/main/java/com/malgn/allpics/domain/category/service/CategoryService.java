package com.malgn.allpics.domain.category.service;

import com.malgn.allpics.domain.category.model.CategoryModifyVo;
import com.malgn.allpics.domain.category.model.CategoryNodeDto;
import com.malgn.allpics.domain.category.model.CategoryRequestDto;
import com.malgn.allpics.domain.category.model.CategoryResponseDto;

import java.util.List;

public interface CategoryService {

    // 카테고리 오더에 따른 카테고리 모두 조회
    List<CategoryRequestDto> selectAllCategories(int categoryOrder);

    // 부모 카테고리에 따른 하위 카테고리 모두 조회
    List<CategoryRequestDto> selectAllSecCategoriesByFirstCategory(Long parentCategoryNum);

    List<CategoryResponseDto> getAllCategoriesOrderByParentNum();

    // 카테고리의 하위 카테고리 모두 조회
    List<CategoryResponseDto> getChildCategories(Long parentCategoryNum);

    List<CategoryNodeDto> getFirstNodeCategories();

    List<CategoryNodeDto> getChildNodeCategories(Long categoryNum);

    String changeInsideSomeCategory(CategoryModifyVo categoryVo);

    String changeAsideSomeCategory(CategoryModifyVo categoryVo);

    String createRootCategory(String categoryName);

    String deleteCategory(CategoryRequestDto categoryRequestDto);
}
