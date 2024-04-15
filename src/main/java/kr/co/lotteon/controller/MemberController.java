package kr.co.lotteon.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MemberController {

    // join (회원 가입 구분) 페이지 매핑
    @GetMapping("/member/join")
    public String join(){
        return "/member/join";
    }
    // login 페이지 매핑
    @GetMapping("/member/login")
    public String login(){
        return "/member/login";
    }
    // register 페이지 매핑
    @GetMapping("/member/register")
    public String register(){
        return "/member/register";
    }
    // registerSeller (판매자 가입) 페이지 매핑
    @GetMapping("/member/registerSeller")
    public String registerSeller(){
        return "/member/registerSeller";
    }
    // signup (약관 동의) 페이지 매핑
    @GetMapping("/member/signup")
    public String signup(){
        return "/member/signup";
    }
}
