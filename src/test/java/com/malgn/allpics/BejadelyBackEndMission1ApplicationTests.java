package com.malgn.allpics;

import com.malgn.allpics.domain.category.entity.Category;
import com.malgn.allpics.domain.category.repository.CategoryRepository;
import com.malgn.allpics.domain.member.repository.MemberRepository;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;


//@SpringBootTest
class BejadelyBackEndMission1ApplicationTests {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    StringEncryptor jasyptStringEncryptor;

    @Autowired
    CategoryRepository categoryRepository;

//    @Test
    void encryptTest() {

        int repeatCount = 0;
        String[] encryptArr = { };

        for(String forEncryptStr : encryptArr ){

            // Repeat Count + 1
            repeatCount += 1;

            // encryption
            String encryptedData = jasyptStringEncryptor.encrypt(forEncryptStr);

            // print

            System.out.println(repeatCount + " : " + encryptedData);

        }
    }

//    @Test
//    void memberEntityBuildTest() {
//
//        Member member = null;
//        String memberId = "abc22";
//
//        try {
//            member = Member.builder()
//                    .memberId(memberId)
//                    .memberPwd("asd23*1231")
//                    .memberName("고창석")
//                    .memberRole("A1")
//                    .build();
//        } catch (Exception e){
//            e.printStackTrace();
//        } finally {
//            memberRepository.save(member);
//
//            Optional<Member> savedMember = memberRepository.findMemberByMemberId(memberId);
//            assertNotNull(savedMember.get().getMemberId());
//        }
//
//    }

//    @Test
    public void findByCategoryOrderTest(){

        List<Category> testCategories = categoryRepository.findByCategoryOrder(1);

        System.out.println(testCategories);

        assertNotNull(testCategories);

    }
}





