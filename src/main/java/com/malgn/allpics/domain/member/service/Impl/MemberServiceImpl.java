package com.malgn.allpics.domain.member.service.Impl;

import com.malgn.allpics.domain.detailcode.entity.DetailCode;
import com.malgn.allpics.domain.detailcode.repository.DetailCodeRepository;
import com.malgn.allpics.domain.member.entity.Member;
import com.malgn.allpics.domain.member.model.MemberRequestDto;
import com.malgn.allpics.domain.member.model.MemberResponseDto;
import com.malgn.allpics.domain.member.repository.MemberRepository;
import com.malgn.allpics.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final DetailCodeRepository detailCodeRepository;
    private final PasswordEncoder passwordEncoder;

    // 모든 회원 조회
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getAllMemberList(Pageable pageable) {

        Map<String, Object> map = new HashMap<>();

        Page<Member> allMemberPage =  memberRepository.findAll(pageable);
        List<MemberResponseDto> allMemberDtoList = new ArrayList<>();

        for(Member member : allMemberPage){
            allMemberDtoList.add(new MemberResponseDto(member));
        }

        int startPage = 0;
        int endPage = 0;
        int blockNumber = 2;

        if(allMemberPage.getNumber() < 3){
            endPage = allMemberPage.getTotalPages() - 1;
        } else if (allMemberPage.getNumber() >= 4 && allMemberPage.getNumber() < endPage - 2){
            startPage = Math.max(0, allMemberPage.getNumber() - blockNumber);
            endPage = Math.min(allMemberPage.getTotalPages() - 1, allMemberPage.getNumber() + blockNumber);
        } else {
            startPage = allMemberPage.getTotalPages() - 5;
            endPage = Math.min(allMemberPage.getTotalPages() - 1, allMemberPage.getNumber() + blockNumber);
        }

        map.put("memberList", allMemberDtoList);
        map.put("page", allMemberPage.getNumber());
        map.put("size", allMemberPage.getSize());
        map.put("totalPages", allMemberPage.getTotalPages());
        map.put("startPage", startPage);
        map.put("endPage", endPage);
        map.put("keyword", null);

        return map;
    }

    @Override
    @Transactional(readOnly = true)
    public MemberResponseDto getMemberDetail(String memberId) {

        Member member = memberRepository.findMemberByMemberId(memberId)
                .orElseThrow(()-> new RuntimeException("Member not found with memberId : " + memberId));

        return new MemberResponseDto(member);
    }

    @Override
    @Transactional
    public String deleteMember(String memberId) {

        memberRepository.deleteByMemberId(memberId);

        return memberId;
    }

    @Override
    @Transactional
    public String updateMember(MemberRequestDto memberRequestDto) {

        Member member = memberRepository.findById(memberRequestDto.memberNum())
                .orElseThrow(()-> new RuntimeException("Member not found with memberNum : " + memberRequestDto.memberNum()));

        if(StringUtils.isNotBlank(memberRequestDto.memberId())){
            member.setMemberId(memberRequestDto.memberId());
        }
        if(StringUtils.isNotBlank(memberRequestDto.memberPwd())){
            member.setMemberPwd(passwordEncoder.encode(memberRequestDto.memberPwd()));
        }
        if(StringUtils.isNotBlank(memberRequestDto.memberName())){
            member.setMemberName(memberRequestDto.memberName());
        }

        return member.getMemberId();
    }

    @Override
    @Transactional
    public String insertMember(MemberRequestDto memberRequestDto) {

        DetailCode detailCode = detailCodeRepository.findById("A1")
                .orElseThrow(()-> new RuntimeException("DetailCode not found with TagId : A1"));

        Member member = Member.builder()
                .memberId(memberRequestDto.memberId())
                .memberPwd(passwordEncoder.encode(memberRequestDto.memberPwd()))
                .memberName(memberRequestDto.memberName())
                .detailCode(detailCode)
                .build();

        memberRepository.save(member);
        detailCode.getMemberList().add(member);
        detailCodeRepository.save(detailCode);

        return member.getMemberId();
    }

    @Override
    public Map<String, Object> searchMemberById(String keyword, Pageable pageable) {

        Map<String, Object> map = new HashMap<>();

        Page<Member> allMemberPage =  memberRepository.findByMemberIdContaining(keyword, pageable);
        List<MemberResponseDto> allMemberDtoList = new ArrayList<>();

        for(Member member : allMemberPage){
            allMemberDtoList.add(new MemberResponseDto(member));
        }

        int startPage = 0;
        int endPage = 0;
        int blockNumber = 2;

        if(allMemberPage.getNumber() < 3){
            endPage = allMemberPage.getTotalPages() - 1;
        } else if (allMemberPage.getNumber() >= 4 && allMemberPage.getNumber() < endPage - 2){
            startPage = Math.max(0, allMemberPage.getNumber() - blockNumber);
            endPage = Math.min(allMemberPage.getTotalPages() - 1, allMemberPage.getNumber() + blockNumber);
        } else {
            startPage = allMemberPage.getTotalPages() - 5;
            endPage = Math.min(allMemberPage.getTotalPages() - 1, allMemberPage.getNumber() + blockNumber);
        }

        map.put("memberList", allMemberDtoList);
        map.put("page", allMemberPage.getNumber());
        map.put("size", allMemberPage.getSize());
        map.put("totalPages", allMemberPage.getTotalPages());
        map.put("startPage", startPage);
        map.put("endPage", endPage);
        map.put("keyword", keyword);

        return map;

    }


}
