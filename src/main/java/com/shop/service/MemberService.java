package com.shop.service;

import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional  //  비즈니스 로직을 담당하는 서비스 계층에 Transactional을 선언해 에러 발생시 변경된 데이터를 로직 수행 전에 콜백 시킴
@RequiredArgsConstructor    //Bean 주입 메소드 = final, @NonNull이 붙은 필드에 생성자 생성. 빈에 생성자가 1개이고 생성자의 파라미터 타입이 빈으로 등록 가능시 @Autowired 어노테이션 사용
public class MemberService implements UserDetailsService {
    //스프링시큐리티에서 UserDetailService를 구현하고 있는 클래스를 통해 로그인 기능 구현

    private final MemberRepository memberRepository;

    public Member saveMember(Member member){
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    //이미 가입된 회원 예외
    private void validateDuplicateMember(Member member){
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember != null){
            throw new IllegalStateException("이미 가입된 회원입니다. ");
        }
    }

    @Override   //로그인할유저의 email 을 파라미터로 전달 받음
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        Member member = memberRepository.findByEmail(email);

        if(member == null){
            throw new UsernameNotFoundException(email);
        }
        return User.builder()   //UserDatail 구현하고 User 객체를 반환, User객체를 생성하기 위해 이메일, 비밀번호, role를 파라미터 넘겨준다.
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }

}
