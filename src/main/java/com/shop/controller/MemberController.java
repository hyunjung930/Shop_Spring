package com.shop.controller;


import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import com.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    //회원가입
    @GetMapping(value = "/new")
    public String memberForm(Model model) {

        model.addAttribute("memberFormDto", new MemberFormDto()); //회원 가입 화면으로부터 넘어오는 가입정보 담아서
        return "member/memberForm"; //타임리프로 넘김
    }

    /**
     * 회원가입 성공 : main으로 리다이렉트
     * 회원정보 검증 및 중복회원 가입 조건에 의해 실패 : 다시 회원가입 페이지로 돌아가 실패이유 출력
     */

    @PostMapping(value = "/new")
    /*검증하려는 객체 앞에 @Vaild 선언하고, bindingResult 객체 추가 후 검사후 결과 담는다.
      bindingResult.hasErrors()를 호출하여 에러가 있다면 회원가입 페이지로 이동
     */
    public String newMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors()){
            return "member/memberForm";
        }
        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder); //회원 생성
            memberService.saveMember(member); //회원 정보 저장
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage()); //중복 회원가입 에러메세지 뷰로 전달
            return "member/memberForm";
        }
        return "redirect:/"; //성공시 리다이렉트
    }

    @GetMapping(value = "/login")
    public String loginMember(){
        return "/member/memberLoginForm";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "/member/memberLoginForm";
    }



}