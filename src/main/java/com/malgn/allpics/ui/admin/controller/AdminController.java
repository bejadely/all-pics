package com.malgn.allpics.ui.admin.controller;

import com.malgn.allpics.domain.category.service.CategoryService;
import com.malgn.allpics.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("admin")
public class AdminController {

    private final MemberService memberService;
    private final CategoryService categoryService;

    @GetMapping("member")
    public String moveToMemberManagementPage(
            Pageable pageable,
            Model model,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "keyword", required = false) String keyword
    ) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("memberNum").descending());
        Map<String, Object> memberMap;

        if(StringUtils.isBlank(keyword)){
            memberMap = memberService.getAllMemberList(pageRequest);
        } else {
            memberMap = memberService.searchMemberById(keyword,pageRequest);
        }

        model.addAttribute("memberList", memberMap.get("memberList"));
        model.addAttribute("paging", memberMap);

        return "admin/all-member";
    }

    @GetMapping("member/{memberId}")
    public String moveToMemberDetailPage(@PathVariable(name = "memberId") String memberId, Model model){

        model.addAttribute("member", memberService.getMemberDetail(memberId));

        return "admin/member-detail";
    }

    @DeleteMapping("member")
    public String deleteMember(@RequestParam(name = "memberId") String memberId, RedirectAttributes rtt){

        String deletedMember = memberService.deleteMember(memberId);
        rtt.addFlashAttribute("deleteMember", deletedMember);

        return "redirect:/admin/member";
    }

    @GetMapping("member/modification/{memberId}")
    public String moveToMemberModifyPage(@PathVariable(name = "memberId") String memberId, Model model){

        model.addAttribute("member", memberService.getMemberDetail(memberId));

        return "admin/member-modify-page";
    }

    @GetMapping("member/add")
    public String moveToMemberAddPage(){

        return "admin/member-add-page";
    }

    @GetMapping("/category")
    public String findAllCategoriesWithChild(Model model){

        model.addAttribute("categoryList", categoryService.getAllCategoriesOrderByParentNum());

        return "admin/category/category-manage-page";
    }

}
