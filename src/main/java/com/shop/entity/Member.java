package com.shop.entity;

import com.shop.constant.Role;
import com.shop.dto.MemberFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import javax.persistence.*;

@Entity
@Table(name = "member")
@Getter
@Setter
@ToString
public class Member {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @Column(unique = true)  //이메일을 통해 구분하기 위해 unique를 사용하여 동일한 값이 들어오지 않게 한다.
    private String email;
    private String password;
    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        //Member 엔티티 생성 메소드. member엔티티에 회원을 생성하는 메소드를 만들어 관리하여 한군데만 수정한다.
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());

        String password = passwordEncoder.encode(memberFormDto.getPassword());  // BCryptPasswordEncoder Bean을 파라미터로 넘겨 비밀번호 암호화
        member.setPassword(password);
        member.setRole(Role.USER);
        return member;
    }

}
