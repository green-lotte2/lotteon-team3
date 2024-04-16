package kr.co.lotteon.controller;

import kr.co.lotteon.entity.member.Terms;
import kr.co.lotteon.service.TermsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final TermsService termsService;

    // signup (약관 동의) 페이지 매핑
    @GetMapping("/member/signup")
    public String signup(Model model,@RequestParam String type){

        log.info("type = "+type);

        //type로 일반회원(normal)인지 판매자(seller)인지 구분
        model.addAttribute("type",type);
        Terms terms = termsService.findByTerms();
        model.addAttribute("terms",terms);

        return "/member/signup";
    }



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


}
