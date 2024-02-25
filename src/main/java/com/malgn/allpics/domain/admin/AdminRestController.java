package com.malgn.allpics.domain.admin;

import com.malgn.allpics.domain.category.model.CategoryModifyVo;
import com.malgn.allpics.domain.category.model.CategoryRequestDto;
import com.malgn.allpics.domain.category.model.CategoryResponseDto;
import com.malgn.allpics.domain.category.service.CategoryService;
import com.malgn.allpics.domain.member.model.MemberRequestDto;
import com.malgn.allpics.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("admin")
public class AdminRestController {

    private final MemberService memberService;
    private final CategoryService categoryService;

    @PutMapping("member")
    public String modifyMemberInfo(@RequestBody MemberRequestDto memberRequestDto) {

        return memberService.updateMember(memberRequestDto);
    }

    @PostMapping("member")
    public String insertNewMember(@RequestBody MemberRequestDto memberRequestDto) {

        return memberService.insertMember(memberRequestDto);
    }

    @GetMapping("category/child/{parentCategoryNum}")
    public List<CategoryResponseDto> getChildCategoriesByParent(@PathVariable(name = "parentCategoryNum") Long parentCategoryNum) {

        return categoryService.getChildCategories(parentCategoryNum);
    }

    @PutMapping("category")
    public String changeCategoryPosition(@RequestBody CategoryModifyVo categoryVo) {

        // 이동된 위치 확인
        if (categoryVo.movedPosition().equals("inside")) {
            // 타겟 카테고리의 하위 카테고리로 변경
            return categoryService.changeInsideSomeCategory(categoryVo);

        } else {
            // 타겟 카테고리를 조회 > 타겟 카테고리의 부모, 오더와 같게 변경

            return categoryService.changeAsideSomeCategory(categoryVo);
        }
    }

    @PostMapping("category")
    public String createNewCategory(@RequestBody CategoryRequestDto categoryRequestDto) {

        return categoryService.createRootCategory(categoryRequestDto.categoryName());
    }

    @DeleteMapping("category")
    public String deleteCategory(@RequestBody CategoryRequestDto categoryRequestDto) {

        return categoryService.deleteCategory(categoryRequestDto);
    }
}
