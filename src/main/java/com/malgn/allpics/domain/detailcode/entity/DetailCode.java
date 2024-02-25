package com.malgn.allpics.domain.detailcode.entity;

import com.malgn.allpics.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.ToString.Exclude;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "detail_codes")
public class DetailCode {

    @Id @Column(name = "detail_code")
    private String codeId;
    private String codeValue;
    private String mainCode;

    @OneToMany(mappedBy = "detailCode", fetch = FetchType.LAZY)
    @Exclude
    private List<Member> memberList = new ArrayList<>();

    public void setMemberList(List<Member> memberList) {
        this.memberList = memberList;
    }
}
