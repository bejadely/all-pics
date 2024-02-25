package com.malgn.allpics.domain.member.repository;

import com.malgn.allpics.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findMemberByMemberId(String id);

    @Override
    Page<Member> findAll(Pageable pageable);

    void deleteByMemberId(String memberId);

    Page<Member> findByMemberIdContaining(String keyword, Pageable pageable);
}
