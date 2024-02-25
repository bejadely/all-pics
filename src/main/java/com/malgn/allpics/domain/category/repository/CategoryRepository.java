package com.malgn.allpics.domain.category.repository;

import com.malgn.allpics.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // 카테고리 오더에 속하는 카테고리 전체 조회
    List<Category> findByCategoryOrder(int categoryOrder);

    // 특정 부모 카테고리에 속하는 하위카테고리 전체 조회
    List<Category> findByParentCategory_CategoryNum(Long parentCategoryNum);

    // 모든 카테고리 조회 (OrderBy 적용)
    @Query("SELECT c FROM Category c ORDER BY c.parentCategory.categoryNum nulls first, c.categoryNum")
    List<Category> findAllCategoriesOrdered();

    // 카테고리 삭제
    void deleteByCategoryNum(Long categoryNum);

}
