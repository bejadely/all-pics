package com.malgn.allpics.ui.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
public class AuthController {

    // 로그인 페이지로 이동
    @GetMapping("/login-form")
    public String moveToLoginPage(Model model,
                                  @RequestParam(value = "error", required = false)String error,
                                  @RequestParam(value = "exception",required = false) String exception,
                                  @RequestParam(value = "expired", required = false) String expired){

        model.addAttribute("loginError", error);
        model.addAttribute("loginException", exception);

        if(expired != null){
            model.addAttribute("duplicateLogin", true);
        }

        return "auth/login-form";
    }

    // 로그아웃 페이지로 이동
    @GetMapping("/logout-page")
    public String moveToLogoutPage(Model model){

        return "auth/logout-page";
    }

    //

}
