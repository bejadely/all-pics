package com.malgn.allpics.domain.image.service.impl;

import com.malgn.allpics.domain.category.entity.Category;
import com.malgn.allpics.domain.category.model.CategoryRequestDto;
import com.malgn.allpics.domain.category.repository.CategoryRepository;
import com.malgn.allpics.domain.image.entity.Image;
import com.malgn.allpics.domain.image.model.ImageRequestDto;
import com.malgn.allpics.domain.image.model.ImageResponseDto;
import com.malgn.allpics.domain.image.repository.ImageRepository;
import com.malgn.allpics.domain.image.service.ImageService;
import com.malgn.allpics.domain.imagetag.entity.ImageTag;
import com.malgn.allpics.domain.imagetag.model.ImageTagSumDto;
import com.malgn.allpics.domain.imagetag.repository.ImageTagRepository;
import com.malgn.allpics.domain.member.entity.Member;
import com.malgn.allpics.domain.member.repository.MemberRepository;
import com.malgn.allpics.domain.tag.entity.Tag;
import com.malgn.allpics.domain.tag.repository.TagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    @Value("${file.upload.path}")
    String uploadImagePath;

    private final ImageRepository imageRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final ImageTagRepository imageTagRepository;

    // 이미지 로컬에 저장
    @Override
    @Transactional
    public ImageResponseDto uploadImage(ImageRequestDto imageRequestDto) {

        // 이미지 경로 없을 시 자동으로 해당 폴더 생성
        File dirForUpload = new File(uploadImagePath);

        try{
            // 경로 존재 확인
            if(!dirForUpload.exists()){
                dirForUpload.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 1. 실제로 저장될 경로 + 난수 + 이름 제작
        String randomPrefix = UUID.randomUUID().toString();
        String savedFileName = randomPrefix + imageRequestDto.file().getOriginalFilename();
        String savedPath = uploadImagePath + randomPrefix + imageRequestDto.file().getOriginalFilename();

        // 파일 저장
        try {
            imageRequestDto.file().transferTo(new File(savedPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 2. 저장한 이미지 경로를 바탕으로 image 객체 정보 db에 저장
        Member member = memberRepository.findMemberByMemberId(imageRequestDto.memberId())
                .orElseThrow(()-> new RuntimeException("Member not found with id : " + imageRequestDto.memberId()));

        // 연관된 카테고리 불러오기
        Category category = categoryRepository.findById(imageRequestDto.categoryNum())
                .orElseThrow(()-> new RuntimeException("Category not found with id : " + imageRequestDto.categoryNum()));

        // DTO를 바탕으로 저장할 이미지 엔티티 생성
        Image image = imageRequestDto.toEntity();
        image.setSavePath(savedFileName);
        image.setMember(member);
        image.setCategory(category);

        // 이미지 저장
        imageRepository.save(image);

        // 반대로 연결되어있는 member의 리스트와 카테고리에 해당 이미지 추가
        member.getImageList().add(image);
        category.getImageList().add(image);

        // 멤버 업데이트 (member의 이미지 리스트가 변경되었음을 jpa에게 알리는 역할)
        memberRepository.save(member);
        categoryRepository.save(category);


        // 3. 태그에 저장 >> 이미지_태그에 저장
        // 널인지 아닌지 일단 체크
        if(!(imageRequestDto.tag().isBlank())){

            // tag 문자열 배열로 전환
            String[] tagArray = imageRequestDto.tag().split(",");

            for(String obj : tagArray){

                Tag tag;

                // 해당 이름의 태그가 있는지 중복 체크
                if(tagRepository.findByTagName(obj).isEmpty()){
                    // 동일한 이름의 태그가 없다면 객체 정보를 바탕으로 엔티티를 생성하고 디비에 새로 저장
                    tag = Tag.builder().tagName(obj).build();
                    tagRepository.save(tag);

                } else {
                    tag = tagRepository.findByTagName(obj)
                            .orElseThrow(() -> new RuntimeException("Tag not found with tagName : " + obj));
                }
                // 이미지_태그 1. 엔티티 생성 및 저장 >  2. 부모인 image와 tag 엔티티의 리스트에 추가해주고, 3. save를 통해 알려줌
                ImageTag imageTag = ImageTag.builder().image(image).tag(tag).tagName(obj).build();
                imageTagRepository.save(imageTag);
                image.getImageTagList().add(imageTag);
                tag.getImageTagList().add(imageTag);
                imageRepository.save(image);
                tagRepository.save(tag);
            }
        }

        return ImageResponseDto.builder()
                .imageNum(image.getImageNum())
                .memberId(image.getMember().getMemberId())
                .categoryNum(image.getCategory().getCategoryNum())
                .savePath(savedFileName)
                .title(image.getTitle())
                .build();
    }

    // 이미지에 해당하는 카테고리 리스트 모두 조회
    public List<CategoryRequestDto> getCategoriesForImage(String imageNum) {
        Image image = imageRepository.findById(imageNum).orElse(null);
        List<CategoryRequestDto> categoryRequestDtoList = new ArrayList<>();
        if (image == null) {
            // 이미지가 존재하지 않는 경우 처리
            return Collections.emptyList();
        }

        for(Category category: getCategoriesRecursive(image.getCategory())){

            // Dto 로 변환
            categoryRequestDtoList.add(new CategoryRequestDto(category));

        }
        return categoryRequestDtoList;
    }

    // 재귀적으로 카테고리 불러오는 메소드
    public List<Category> getCategoriesRecursive(Category category) {
        List<Category> categories = new ArrayList<>();
        collectCategories(category, categories);
        Collections.reverse(categories);
        return categories;
    }

    public void collectCategories(Category category, List<Category> categories) {
        if (category != null) {
            categories.add(category);
            collectCategories(category.getParentCategory(), categories);
        }
    }

    // 모든 하위카테고리 조회
    public List<Long> findAllSubcategoryIds(Category category) {
        List<Long> allSubcategoryIds = new ArrayList<>();
        findSubcategoryIdsRecursive(category, allSubcategoryIds);
        return allSubcategoryIds;
    }

    private void findSubcategoryIdsRecursive(Category category, List<Long> result) {
        result.add(category.getCategoryNum());
        List<Category> subcategories = category.getSubCategoryList();
        for (Category subcategory : subcategories) {
            findSubcategoryIdsRecursive(subcategory, result);
        }
    }

    // 이미지 상세 정보 단건 조회
    @Override
    public ImageResponseDto getImageDetailByImageNum(String imageNum) {

        Image image = imageRepository.findById(imageNum)
                .orElseThrow(() ->new RuntimeException("Image not found with imageNum : " + imageNum));

        return ImageResponseDto.builder()
                .imageNum(imageNum)
                .savePath(image.getSavePath())
                .title(image.getTitle())
                .memberId(image.getMember().getMemberId())
                .createdDate(image.getCreatedDate())
                .lastModifiedDate(image.getLastModifiedDate())
                .build();
    }

    // 이미지에 속하는 태그 리스트 조회
    @Override
    public List<String> getTagListForImage(String imageNum) {

        Image image = imageRepository.findById(imageNum)
                .orElseThrow(()-> new RuntimeException("Image not found with imageNum : " + imageNum));

        List<ImageTag> imageTagList = image.getImageTagList();
        // tagCount가 같이 있는 값 필요
        List<String> tagList = new ArrayList<>();

        for(ImageTag imageTag : imageTagList){
            tagList.add(imageTag.getTagName());
        }

        return tagList;
    }

    // 이미지에 속하는 태그 리스트 count와 함께 조회
    @Override
    public List<ImageTagSumDto> getTagListWithSumForImage(String imageNum) {

        // tagCount가 같이 있는 값 필요
        List<ImageTagSumDto> tagSumList = imageTagRepository.findByTagNameWithSumForImage(imageNum);
        System.out.println(tagSumList);

        return tagSumList;
    }

    @Override
    public String deleteImage(String imageNum) {

        imageRepository.deleteById(imageNum);

        return "success";
    }

    @Override
    public List<ImageTagSumDto> getAllTagListWithSum() {

        return imageTagRepository.findAllTagNameWithSum();
    }

    @Override
    @Transactional
    public Map<String, Object> getAllImages(Pageable pageable) {

        Map<String, Object> map = new HashMap<>();

        Page<Image> allImagePage =  imageRepository.findAll(pageable);
        List<ImageResponseDto> allImageDtoList = new ArrayList<>();

        for(Image image : allImagePage){
            allImageDtoList.add(new ImageResponseDto(image));
        }

        int startPage = 0;
        int endPage = 0;
        int blockNumber = 2;

        if(allImagePage.getNumber() < 3){
            endPage = allImagePage.getTotalPages() - 1;
        } else if (allImagePage.getNumber() >= 4 && allImagePage.getNumber() < endPage - 2){
            startPage = Math.max(0, allImagePage.getNumber() - blockNumber);
            endPage = Math.min(allImagePage.getTotalPages() - 1, allImagePage.getNumber() + blockNumber);
        } else {
            startPage = allImagePage.getTotalPages() - 5;
            endPage = Math.min(allImagePage.getTotalPages() - 1, allImagePage.getNumber() + blockNumber);
        }

        map.put("imageList", allImageDtoList);
        map.put("page", allImagePage.getNumber());
        map.put("size", allImagePage.getSize());
        map.put("totalPages", allImagePage.getTotalPages());
        map.put("startPage", startPage);
        map.put("endPage", endPage);
        map.put("keyword", null);

        return map;
    }

    @Override
    public Map<String, Object> getImagesWithTitle(String keyword, Pageable pageable) {

        Map<String, Object> map = new HashMap<>();

        Page<Image> allImagePage =  imageRepository.findByTitleContaining(keyword, pageable);
        List<ImageResponseDto> allImageDtoList = new ArrayList<>();

        for(Image image : allImagePage){
            allImageDtoList.add(new ImageResponseDto(image));
        }

        int startPage = 0;
        int endPage = 0;
        int blockNumber = 2;

        if(allImagePage.getNumber() < 3){
            endPage = allImagePage.getTotalPages() - 1;
        } else if (allImagePage.getNumber() >= 4 && allImagePage.getNumber() < endPage - 2){
            startPage = Math.max(0, allImagePage.getNumber() - blockNumber);
            endPage = Math.min(allImagePage.getTotalPages() - 1, allImagePage.getNumber() + blockNumber);
        } else {
            startPage = allImagePage.getTotalPages() - 5;
            endPage = Math.min(allImagePage.getTotalPages() - 1, allImagePage.getNumber() + blockNumber);
        }

        map.put("imageList", allImageDtoList);
        map.put("page", allImagePage.getNumber());
        map.put("size", allImagePage.getSize());
        map.put("totalPages", allImagePage.getTotalPages());
        map.put("startPage", startPage);
        map.put("endPage", endPage);
        map.put("keyword", keyword);
        map.put("categoryNum", 0);
        map.put("tagNames", new ArrayList<>());

        return map;
    }


    // 다중 검색
    @Override
    public Map<String, Object> searchImagesWithConditions(String keyword, Long categoryNum, List<String> tagNames, Pageable pageable) {

        // 1. category의 하위 카테고리 리스트 뽑아오는 구문 실행
        List<Long> categoryNums = null;

        if(categoryNum > 0){

            Category topCategory = categoryRepository.findById(categoryNum)
                    .orElseThrow(() -> new RuntimeException("Category not found with id : " + categoryNum));

            categoryNums = findAllSubcategoryIds(topCategory);

        }

        if(tagNames.isEmpty()) tagNames = null;

        // 2. 다중조건 검색 실행

        Map<String, Object> map = new HashMap<>();

        Page<Image> allImagePage =  imageRepository.searchImages(keyword, categoryNums, tagNames, pageable);
        List<ImageResponseDto> allImageDtoList = new ArrayList<>();

        // 태그 조건이 모두 만족되는 image만 추출

        if(tagNames != null){
            for(Image image : allImagePage){
                // 검색하고자 하는 태그가 모두 들어가 있는지 확인하기 위한 int
                int tagContainCount = 0;
                // 뽑아낸 애들 중 이미지 하나당 가지고 있는 image의 태그 객체 리스트 추출
                List<ImageTag> imageTagList = image.getImageTagList();
                // 반복문을 통해 image의 태그 객체에서 태그 이름을 하나씩 추출해서 담음
                List<String> searchedTagNames = new ArrayList<>();
                for(ImageTag imageTag : imageTagList){
                    searchedTagNames.add(imageTag.getTagName());
                }
                // 담아낸 애들이 태그 리스트와 다 일치하는게 있는지 반복하면서 확인
                for(String conditionTag : tagNames){
                    if(searchedTagNames.contains(conditionTag)){
                        tagContainCount++;
                    }
                }

                if(tagContainCount == tagNames.size()){
                    allImageDtoList.add(new ImageResponseDto(image));
                }
            }
        } else {
            for(Image image : allImagePage){
                allImageDtoList.add(new ImageResponseDto(image));
            }
        }

        int startPage = 0;
        int endPage = 0;
        int blockNumber = 2;

        if(allImagePage.getNumber() < 3){
            endPage = allImagePage.getTotalPages() - 1;
        } else if (allImagePage.getNumber() >= 4 && allImagePage.getNumber() < endPage - 2){
            startPage = Math.max(0, allImagePage.getNumber() - blockNumber);
            endPage = Math.min(allImagePage.getTotalPages() - 1, allImagePage.getNumber() + blockNumber);
        } else {
            startPage = allImagePage.getTotalPages() - 5;
            endPage = Math.min(allImagePage.getTotalPages() - 1, allImagePage.getNumber() + blockNumber);
        }

        map.put("imageList", allImageDtoList);
        map.put("page", allImagePage.getNumber());
        map.put("size", allImagePage.getSize());
        map.put("totalPages", allImagePage.getTotalPages());
        map.put("startPage", startPage);
        map.put("endPage", endPage);
        map.put("keyword", keyword);
        map.put("categoryNum", categoryNum);
        map.put("tagNames", tagNames);

        return map;
    }

    @Override
    @Transactional
    public String modifyImage(ImageRequestDto imageRequestDto) {

        System.out.println(imageRequestDto.imageNum());

        // 해당 이미지 찾기
        Image imgToUpdate = imageRepository.findById(imageRequestDto.imageNum())
                .orElseThrow(()->new RuntimeException("Image not found with imageNum : " + imageRequestDto.imageNum()));

        // 제목 수정
        if(StringUtils.isNotBlank(imageRequestDto.title())){
            imgToUpdate.setTitle(imageRequestDto.title());
        }

        Category newCategory;

        if(imageRequestDto.categoryNum() != null){
            // 속할 카테고리 찾기
            newCategory = categoryRepository.findById(imageRequestDto.categoryNum())
                    .orElseThrow(()->new RuntimeException("Category not found with categoryNum : " + imageRequestDto.categoryNum()));
            // 카테고리 정보 변경
            imgToUpdate.setCategory(newCategory);
        }

//        // 태그 정보 변경
        if(!(imageRequestDto.tagNames().isEmpty())){

            imageTagRepository.deleteByImage(imgToUpdate);
            // 기존 태그 삭제

            // 새 태그 추가
            for(String tagName : imageRequestDto.tagNames()){
                Tag tag = tagRepository.findByTagName(tagName)
                        .orElseGet(() -> tagRepository.save(Tag.builder().tagName(tagName).build()));
                imgToUpdate.getImageTagList().add(ImageTag.builder().image(imgToUpdate).tag(tag).tagName(tagName).build());
            }

        }

        // 3. save를 통해 이미지 및 관련 엔티티에게 변경사항 알림
//        imageRepository.save(imgToUpdate);


        return "success";
    }
}
