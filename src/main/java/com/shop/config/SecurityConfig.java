package com.shop.config;

import com.shop.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
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
        http.authorizeRequests()
                .mvcMatchers("/","/member/**","/item/**","/images/**").permitAll()
                //permitAll()을 통해 모든 사용자가 인증 없이 해당 경로에 접근할 수 있도록 설정, 메인이지, 회원 관련 URL, 상품 상세 페이지, 상품 이미지 불러오는 경로
                .mvcMatchers("/admin/**").hasRole("ADMIN")
                // /admin 으로 시작하는 경로는 해당 계정이 ADMIN Role일 경우에만 접근 가능하도록 설정
                .anyRequest().authenticated();
                // 인증되지 않은 사용자가 리소스에 접근시 수행되는 핸들러 등록.
        http.exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());
    }

    @Override
    public void configure(WebSecurity web) throws Exception{
        web.ignoring().antMatchers("/css/**","/js/**","/img/**");   // static 디렉토리의 하위 파일은 인증 무시하도록 설정
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
