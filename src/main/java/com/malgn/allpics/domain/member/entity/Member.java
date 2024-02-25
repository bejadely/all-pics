package com.malgn.allpics.domain.member.entity;

import com.malgn.allpics.common.entity.BaseTime;
import com.malgn.allpics.domain.detailcode.entity.DetailCode;
import com.malgn.allpics.domain.image.entity.Image;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "members")
public class Member extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberNum;
    private String memberId;
    private String memberPwd;
    private String memberName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_role")
    private DetailCode detailCode;

    @OneToMany(mappedBy = "member")
    List<Image> imageList = new ArrayList<>();


    @Builder
    private Member(Long memberNum, String memberId, String memberPwd, String memberName, DetailCode detailCode, LocalDateTime lastModifiedDate, LocalDateTime deletedDate) {

        // memberId 유효성 검사
        checkArgument(StringUtils.isNotBlank(memberId), "memberId must be provided.");
        checkArgument(memberId.matches("^[a-zA-Z0-9]*$"), "memberId should be composed only of English letters and numbers.");
        checkArgument(memberId.matches("^[a-zA-Z0-9]{4,18}$"), "memberId must consist of 4 to 18 characters in length.");

        // 비밀번호 유효성 검사
        checkArgument(StringUtils.isNotBlank(memberId), "memberPwd must be provided.");

        // 이름 유효성 검사
        checkArgument(StringUtils.isNotBlank(memberName), "memberName must be provided.");
        checkArgument(memberName.matches("^[a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣]*$") ,"memberName composed only of English letters and Korean letters");
        checkArgument(memberName.matches("^[a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣]{1,17}$") ,"memberName must consist of 1 to 17 characters in length.");

        // 권한 유효성 검사
        checkArgument(StringUtils.isNotBlank(detailCode.getCodeId()), "memberRole must be provided.");
        checkArgument(detailCode.getCodeId().matches("^A[0-9]$"), "memberRole must start with 'A' and end with a digit.");

        this.memberNum = memberNum;
        this.memberId = memberId;
        this.memberPwd = memberPwd;
        this.memberName = memberName;
        this.detailCode = detailCode;
        this.lastModifiedDate = lastModifiedDate;
        this.deletedDate = deletedDate;

    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public void setMemberPwd(String memberPwd) {
        this.memberPwd = memberPwd;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

}

