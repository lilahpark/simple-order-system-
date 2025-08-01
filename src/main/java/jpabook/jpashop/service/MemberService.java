package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    //필드 인젝션
    private final  MemberRepository memberRepository;

    //롬복 @RequiredArgsConstructor 어노테이션이 생성자를 만들어주는 역할을 한다
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }





    /**
     * 회원가입
     * @param member
     * @return
     */
    @Transactional
    public Long join(Member member) {

        validateDuplicateMember(member); //중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        //Exception
       List<Member> findMembers =  memberRepository.findByName(member.getName());
       if(!findMembers.isEmpty()) {
           throw new IllegalStateException("이미 존재하는 회원입니다.");
       }
    }
    //회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }
    public Member findOne(Long memberId ) {
        return memberRepository.findOne(memberId);
    }


    @Transactional
    public void update(Long id, String name ) {

        Member member = memberRepository.findOne(id);
        member.setName(name );
    }
}
