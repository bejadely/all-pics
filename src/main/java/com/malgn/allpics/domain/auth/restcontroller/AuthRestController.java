package com.malgn.allpics.domain.auth.restcontroller;

import com.malgn.allpics.security.service.impl.MemberDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthRestController {

    private final SessionRegistry sessionRegistry;
    private final MemberDetailService memberDetailService;

    // session 값 체크
    @GetMapping("/session")
    public boolean ajaxCheckSession(@RequestParam String memberId){

        if(memberId != null){
            UserDetails userDetails = memberDetailService.loadUserByUsername(memberId);

            List<SessionInformation> allSessions = sessionRegistry.getAllSessions(userDetails, false);

            if(allSessions.size() > 0){
                // 다른 곳에서 로그인 한 세션값 있는 경우
                return true;
            } else {
                // 다른 곳에서 로그인 한 세션값 없는 경우
                return false;
            }
        } else {
            return false;
        }
    }
}
