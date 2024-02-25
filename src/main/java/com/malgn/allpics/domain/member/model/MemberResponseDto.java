package com.malgn.allpics.domain.member.model;

import com.malgn.allpics.domain.member.entity.Member;

import java.time.LocalDateTime;

public record MemberResponseDto(
        Long memberNum,
        String memberId,
        String memberName,
        String memberRole,
        LocalDateTime createdDate,
        LocalDateTime lastModifiedDate,
        LocalDateTime deletedDate
) {
    public MemberResponseDto(Member member){
        this(
                member.getMemberNum(),
                member.getMemberId(),
                member.getMemberName(),
                member.getDetailCode().getCodeId(),
                member.getCreatedDate(),
                member.getLastModifiedDate(),
                member.getDeletedDate()
        );
    }
}
