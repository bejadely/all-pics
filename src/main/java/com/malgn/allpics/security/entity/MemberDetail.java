package com.malgn.allpics.security.entity;

import com.malgn.allpics.domain.member.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MemberDetail implements UserDetails {

    private final Member member;

    public MemberDetail(Member member){ this.member = member; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> auth = new ArrayList<>();
        auth.add(new SimpleGrantedAuthority("ROLE_" + member.getDetailCode().getCodeId()));
        return auth;
    }

    @Override
    public String getPassword() {
        return member.getMemberPwd();
    }

    @Override
    public String getUsername() {
        return member.getMemberId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof MemberDetail){
            return this.getUsername().equals(((MemberDetail) obj).getUsername());
        }
        return false;
    }

    @Override
    public int hashCode(){
        return this.getUsername().hashCode();
    }

}
