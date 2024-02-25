package com.malgn.allpics.domain.category.restcontroller;

import com.malgn.allpics.domain.category.model.CategoryNodeDto;
import com.malgn.allpics.domain.category.model.CategoryRequestDto;
import com.malgn.allpics.domain.category.repository.CategoryRepository;
import com.malgn.allpics.domain.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("category")
public class CategoryRestController {

    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    // 1차 카테고리 선택에 따른 2차 카테고리 조회
    @GetMapping("second-category/{parentCategoryNum}")
    public List<CategoryRequestDto> loadSecondCategoriesByParentCategory(@PathVariable("parentCategoryNum") Long parentCategoryNum){

        // 부모 카테고리를 조건으로 2차 카테고리 리스트 전송
        return categoryService.selectAllSecCategoriesByFirstCategory(parentCategoryNum);
    }

    @GetMapping("node")
    public List<CategoryNodeDto> getSubCategoriesByNode(@RequestParam(name="node", required = false) Long categoryNum){

        if(categoryNum == null){
            return categoryService.getFirstNodeCategories();
        } else {
            return categoryService.getChildNodeCategories(categoryNum);
        }

    }





}
