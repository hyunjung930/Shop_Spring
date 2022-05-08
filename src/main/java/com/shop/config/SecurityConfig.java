package com.shop.config;

import com.shop.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    MemberService memberService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //http 요청에 대한 보안 설정 페이지 권한 설정, 로그인 페이지 설정, 로그아웃 메소드 등에 대한 설정 작성
        http.formLogin()
                .loginPage("/members/login")    //로그인 페이지 URL 설정
                .defaultSuccessUrl("/")     //로그인 성공 시 이동할 URL 설정
                .usernameParameter("email") //로그인 시 사용할 파라미터 이름을 email로 지정
                .failureUrl("/members/login/error") //로그인 실패시 이동할 URL 이동
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout")) //로그아웃 URL 설정
                .logoutSuccessUrl("/"); //로그아웃 성공시 이동할 URL Set
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        //BCryptPasswordEncoder의 해시 함수를 이용해 비밀번호를 암호화 하여 저장(빈으로 등록하여 사용)
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(memberService)
                .passwordEncoder(passwordEncoder());
    }

}
