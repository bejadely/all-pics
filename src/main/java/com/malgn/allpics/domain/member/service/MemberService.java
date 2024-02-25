package com.malgn.allpics.domain.member.service;

import com.malgn.allpics.domain.member.model.MemberRequestDto;
import com.malgn.allpics.domain.member.model.MemberResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface MemberService {

    Map<String, Object> getAllMemberList(Pageable pageable);

    MemberResponseDto getMemberDetail(String memberId);
    String deleteMember(String memberId);

    String updateMember(MemberRequestDto memberRequestDto);

    String insertMember(MemberRequestDto memberRequestDto);

    Map<String, Object> searchMemberById(String keyword, Pageable pageable);
}
