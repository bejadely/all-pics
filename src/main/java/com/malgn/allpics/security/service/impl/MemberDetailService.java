package com.malgn.allpics.security.service.impl;

import com.malgn.allpics.domain.member.entity.Member;
import com.malgn.allpics.domain.member.repository.MemberRepository;
import com.malgn.allpics.security.entity.MemberDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class MemberDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findMemberByMemberId(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return new MemberDetail(member);

    }
}
