package com.malgn.allpics.domain.member.model;

import java.time.LocalDateTime;

public record MemberRequestDto(
        Long memberNum,
        String memberId,
        String memberPwd,
        String memberName,
        String memberRole,
        LocalDateTime createdDate,
        LocalDateTime lastModifiedDate,
        LocalDateTime deletedDate
){}
