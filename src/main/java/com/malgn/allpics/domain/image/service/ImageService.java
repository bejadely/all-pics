package com.malgn.allpics.domain.image.service;

import com.malgn.allpics.domain.category.entity.Category;
import com.malgn.allpics.domain.category.model.CategoryRequestDto;
import com.malgn.allpics.domain.image.model.ImageRequestDto;
import com.malgn.allpics.domain.image.model.ImageResponseDto;
import com.malgn.allpics.domain.imagetag.model.ImageTagSumDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ImageService {

    // 이미지 파일 업로드
    ImageResponseDto uploadImage(ImageRequestDto imageRequestDto);

    // 이미지 카테고리 리스트 불러오기
    List<CategoryRequestDto> getCategoriesForImage(String imageNum);

    List<Category> getCategoriesRecursive(Category category);

    void collectCategories(Category category, List<Category> categories);

    // 이미지 상세 정보 단건 조회
    ImageResponseDto getImageDetailByImageNum(String imageNum);

    // 이미지 태그 리스트 불러오기
    List<String> getTagListForImage(String imageNum);

    // 모든 이미지 조회
    Map<String, Object> getAllImages(Pageable pageable);

    // 키워드 조회
    Map<String, Object> getImagesWithTitle(String keyword, Pageable pageable);

    Map<String, Object> searchImagesWithConditions(String keyword, Long categoryNums, List<String> tagNames, Pageable pageable);

    String modifyImage(ImageRequestDto imageRequestDto);

    List<ImageTagSumDto> getTagListWithSumForImage(String imageNum);

    String deleteImage(String imageNum);

    List<ImageTagSumDto> getAllTagListWithSum();
}
