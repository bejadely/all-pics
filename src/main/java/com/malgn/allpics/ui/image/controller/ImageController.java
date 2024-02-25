package com.malgn.allpics.ui.image.controller;

import com.malgn.allpics.domain.category.service.CategoryService;
import com.malgn.allpics.domain.image.model.ImageRequestDto;
import com.malgn.allpics.domain.image.model.ImageResponseDto;
import com.malgn.allpics.domain.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("image")
public class ImageController {

    private final CategoryService categoryService;
    private final ImageService imageService;

    // image 상세조회 페이지로 이동
    @GetMapping("{imageNum}")
    public String moveToImageDetailPage(@PathVariable("imageNum") String imageNum, Model model){

        // 이미지 정보 저장
        model.addAttribute("image", imageService.getImageDetailByImageNum(imageNum));

        // 카테고리 정보 저장
        model.addAttribute("categoryList", imageService.getCategoriesForImage(imageNum));

        // 태그 정보 저장
        model.addAttribute("tagList", imageService.getTagListForImage(imageNum));

        model.addAttribute("tagSumList", imageService.getTagListWithSumForImage(imageNum));


        return "image/image-detail";
    }

    // image 등록 페이지로 이동
    @GetMapping("upload")
    public String moveToImageUploadPage(Model model){

        model.addAttribute("firstCategories", categoryService.selectAllCategories(1));

        return "image/image-upload-page";
    }

    // image 등록
    @PostMapping(value = "")
    public String uploadImage(ImageRequestDto imageRequestDto){

        // 이미지 업로드 기능 수행
        ImageResponseDto imageResponseDto = imageService.uploadImage(imageRequestDto);

        return "redirect:/image/" + imageResponseDto.imageNum();
    }

    @GetMapping("search")
    public String searchWithDetailConditions(
            Pageable pageable,
            Model model,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "6") Integer size,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "categoryNum", required = false) String categoryNum,
            @RequestParam(name = "tagNames", required = false) List<String> tagNames
    ){
        // 이미지 상세 검색
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("imageNum").descending());

        Map<String, Object> imageMap = imageService.searchImagesWithConditions(keyword, Long.valueOf(categoryNum), tagNames, pageRequest);

        model.addAttribute("imageList", imageMap.get("imageList"));
        model.addAttribute("paging", imageMap);
        model.addAttribute("allTagList", imageService.getAllTagListWithSum());

        return "main/main";
    }


    // 이미지 수정 페이지로 이동
    @GetMapping("/modify/{imageNum}")
    public String moveToImageModificationPage(Model model,
                                              @PathVariable(name = "imageNum") String imageNum){

        // 이미지 정보 저장
        model.addAttribute("image", imageService.getImageDetailByImageNum(imageNum));

        // 카테고리 정보 저장
        model.addAttribute("categoryList", imageService.getCategoriesForImage(imageNum));

        // 태그 정보 저장
        model.addAttribute("tagList", imageService.getTagListForImage(imageNum));
        System.out.println(imageService.getTagListForImage(imageNum));

        return "image/image-modify-page";
    }

    // 이미지 수정 기능 수행
    @PutMapping("")
    @ResponseBody
    public String modifyImage(@RequestBody ImageRequestDto imageRequestDto,
                              Model model){

        // 이미지 수정 기능 수행
        String message = imageService.modifyImage(imageRequestDto);

        return message;
    }

    @DeleteMapping("")
    public String deleteImage(String imageNum, RedirectAttributes rtt){

        String deleteMessage = imageService.deleteImage(imageNum);
        rtt.addFlashAttribute("deleteMessage", "삭제가 성공적으로 완료되었습니다.");

        return "redirect:/";
    }

}
