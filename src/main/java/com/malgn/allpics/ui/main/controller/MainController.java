package com.malgn.allpics.ui.main.controller;

import com.malgn.allpics.domain.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ImageService imageService;

    @GetMapping("/")
    public String moveToMainPage(
            Pageable pageable,
            Model model,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "6") int size,
            @RequestParam(name = "keyword", required = false) String keyword
    ){

        // 모든 이미지 조회 및 페이징
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("imageNum").descending());
        Map<String, Object> imageMap;

        // 키워드 검색
        if(StringUtils.isNotBlank(keyword)){
            // 키워드 검색하는 메소드 호출
            imageMap = imageService.getImagesWithTitle(keyword, pageRequest);
        } else{
            imageMap = imageService.getAllImages(pageRequest);
        }

        model.addAttribute("imageList", imageMap.get("imageList"));
        model.addAttribute("paging", imageMap);
        model.addAttribute("allTagList", imageService.getAllTagListWithSum());

        return "main/main";
    }
}
