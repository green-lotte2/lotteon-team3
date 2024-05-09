package kr.co.lotteon.controller;

import kr.co.lotteon.entity.member.Terms;
import kr.co.lotteon.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class PolicyController {

    private final MemberService memberService;
    @GetMapping("/policy/terms")
    public String buyer(Model model, String termsType) {

        model.addAttribute("termsType", termsType);
        Terms terms = memberService.findByTerms();
        model.addAttribute("terms", terms);
        return "/policy/terms";
    }
}
