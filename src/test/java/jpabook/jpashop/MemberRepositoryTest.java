package jpabook.jpashop;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;


import static org.junit.jupiter.api.Assertions.assertEquals;

//@SpringBootTest
//public class MemberRepositoryTest {

//    @Autowired
//    private MemberRepository memberRepository;
//
//    @Test
//    @Transactional
//    @Rollback(false)
//    public void testMember() throws Exception {
//        // given
//        Member member = new Member();
//        member.setUserName("memberA");
//
//        // when
//        Long savedId = memberRepository.save(member);
//        Member findMember = memberRepository.find(savedId);
//
//        // then
//        assertEquals(member.getId(), findMember.getId());
//        assertEquals(member.getUserName(), findMember.getUserName());
//        assertThat(findMember).isEqualTo(member);
//    }
//}