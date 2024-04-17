package kr.co.lotteon.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.lotteon.dto.member.MemberDTO;
import kr.co.lotteon.entity.member.Terms;
import kr.co.lotteon.service.member.MemberService;
import kr.co.lotteon.service.member.TermsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final TermsService termsService;
    private final MemberService memberService;

    // signup (약관 동의) 페이지 매핑
    @GetMapping("/member/signup")
    public String signup(Model model,@RequestParam String type){

        log.info("약관동의 type = "+type);

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
    public String register(Model model, String type){

        log.info("회원가입 type = "+type);

        model.addAttribute("type", type);
        // type이 판매자(seller)로 들어오면 판매자 회원가입 페이지로 리다이렉트
        if(type.equals("seller")){
            return "redirect:/member/registerSeller?type=seller";
        }
        
        return "/member/register";
    }
    // 회원 가입 처리 - DB 전송
    @PostMapping("/member/register")
    public String register(MemberDTO memberDTO, HttpServletRequest request, HttpServletResponse response){

        memberDTO.setRegip(request.getRemoteAddr());
        memberService.save(memberDTO);

        return "redirect:/member/login?success=200";
    }

    // registerSeller (판매자 가입) 페이지 매핑
    @GetMapping("/member/registerSeller")
    public String registerSeller(){
        return "/member/registerSeller";
    }


}
