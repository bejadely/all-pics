package com.malgn.allpics.domain.category.service.impl;

import com.malgn.allpics.domain.category.entity.Category;
import com.malgn.allpics.domain.category.model.CategoryModifyVo;
import com.malgn.allpics.domain.category.model.CategoryNodeDto;
import com.malgn.allpics.domain.category.model.CategoryRequestDto;
import com.malgn.allpics.domain.category.model.CategoryResponseDto;
import com.malgn.allpics.domain.category.repository.CategoryRepository;
import com.malgn.allpics.domain.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    // 차수에 해당하는 카테고리 모두 조회
    @Override
    @Transactional(readOnly = true)
    public List<CategoryRequestDto> selectAllCategories(int categoryOrder) {

        List<Category> categories = categoryRepository.findByCategoryOrder(categoryOrder);
        List<CategoryRequestDto> firstCategoryRequestDtoList = new ArrayList<>();

        for(Category categoryEntity : categories){

            CategoryRequestDto categoryRequestDto = new CategoryRequestDto(categoryEntity);
            firstCategoryRequestDtoList.add(categoryRequestDto);

        }
        return firstCategoryRequestDtoList;
    }

    // todo 리팩토링 : 부모 카테고리 넘버에 따른 직계 자식 카테고리 모두 조회
    @Override
    @Transactional
    public List<CategoryRequestDto> selectAllSecCategoriesByFirstCategory(Long parentCategoryNum) {

        List<Category> categories = categoryRepository.findByParentCategory_CategoryNum(parentCategoryNum);
        List<CategoryRequestDto> secondCategoryRequestDtoList = new ArrayList<>();
        // json 변환 실험

        for(Category categoryEntity : categories){

            CategoryRequestDto categoryRequestDto = new CategoryRequestDto(categoryEntity);
            secondCategoryRequestDtoList.add(categoryRequestDto);
        }

        return secondCategoryRequestDtoList;
    }

    @Override
    public List<CategoryResponseDto> getAllCategoriesOrderByParentNum() {

        List<CategoryResponseDto> categoryDtoList = new ArrayList<>();

        for (Category category : categoryRepository.findAllCategoriesOrdered()) {
            CategoryResponseDto categoryResponseDto = new CategoryResponseDto(category);
            categoryDtoList.add(categoryResponseDto);
        }

        return categoryDtoList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getChildCategories(Long parentCategoryNum) {

        Category parentCategory = categoryRepository.findById(parentCategoryNum)
                .orElseThrow(()-> new RuntimeException("ChildCategories not found with parentCategoryNum : " + parentCategoryNum));
        List<CategoryResponseDto> childCategories = new ArrayList<>();

        for(Category category : parentCategory.getSubCategoryList()){

            CategoryResponseDto categoryResponseDto = new CategoryResponseDto(category);
            childCategories.add(categoryResponseDto);

        }

        return childCategories;
    }

    @Override
    public List<CategoryNodeDto> getFirstNodeCategories() {

        List<Category> categories = categoryRepository.findByCategoryOrder(1);
        List<CategoryNodeDto> categoryNodeDtos = new ArrayList<>();

        for(Category category : categories){
            categoryNodeDtos.add(new CategoryNodeDto(category));
        }

        return categoryNodeDtos;
    }

    @Override
    public List<CategoryNodeDto> getChildNodeCategories(Long categoryNum) {

        Category category = categoryRepository.findById(categoryNum)
                .orElseThrow(()-> new RuntimeException("Category not found with categoryNum : " + categoryNum));

        List<Category> categories = category.getSubCategoryList();

        List<CategoryNodeDto> categoryNodeDtos = new ArrayList<>();

        for(Category subCategory : categories){
            categoryNodeDtos.add(new CategoryNodeDto(subCategory));
        }

        return categoryNodeDtos;
    }

    // 특정 카테고리의 하위 카테고리로 값 변경
    @Override
    @Transactional
    public String changeInsideSomeCategory(CategoryModifyVo categoryVo) {

        Category category = categoryRepository.findById(categoryVo.movedCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with categoryNum : " + categoryVo.movedCategoryId()));

        // 부모로 삼을 카테고리 호출
        Category parentCategory = categoryRepository.findById(categoryVo.targetCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with categoryNum : " + categoryVo.movedCategoryId()));

        // 변환
        category.setParentCategory(parentCategory);
        category.setCategoryOrder(parentCategory.getCategoryOrder() + 1);

        // 부모 카테고리에 하위 카테고리를 add
        parentCategory.getSubCategoryList().add(category);

        // 당사자 변경사항 저장
        categoryRepository.save(category);
        categoryRepository.save(parentCategory);

        return "success:inside";
    }

    @Override
    public String changeAsideSomeCategory(CategoryModifyVo categoryVo) {

        Category category = categoryRepository.findById(categoryVo.movedCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with categoryNum : " + categoryVo.movedCategoryId()));

        // 병렬관계로 삼을 카테고리 호출
        Category siblingCategory = categoryRepository.findById(categoryVo.targetCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with categoryNum : " + categoryVo.movedCategoryId()));

        // 부모 관계로 삼을 카테고리 호출

        // 변환
        category.setParentCategory(siblingCategory.getParentCategory());
        category.setCategoryOrder(siblingCategory.getCategoryOrder());

        // 부모 카테고리에 하위 카테고리를 add
        if(siblingCategory.getParentCategory() == null){

        } else {
            siblingCategory.getParentCategory().getSubCategoryList().add(category);
            categoryRepository.save(siblingCategory.getParentCategory());
        }

        categoryRepository.save(category);

        return "success:aside";
    }

    @Override
    @Transactional
    public String createRootCategory(String categoryName) {

        Category newRootCategory = Category.builder()
                .categoryName(categoryName)
                .categoryOrder(1)
                .build();
        categoryRepository.save(newRootCategory);

        return "success";
    }

    @Override
    @Transactional
    public String deleteCategory(CategoryRequestDto categoryRequestDto) {

        // 카테고리 삭제
        categoryRepository.deleteByCategoryNum(categoryRequestDto.categoryNum());

        return "deleted";
    }
}
