package kr.co.lotteon.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class PolicyController {

    // buyer (구매 회원 약관) 페이지 매핑
    @GetMapping("/policy/buyer")
    public String buyer(){

        return "/policy/buyer";
    }
    // seller (판매 회원 약관) 페이지 매핑
    @GetMapping("/policy/seller")
    public String seller(){
        return "/policy/seller";
    }
    // finance (전자 금융 거래 약관) 페이지 매핑
    @GetMapping("/policy/finance")
    public String finance(){
        return "/policy/finance";
    }
    // location (위치 정보 약관) 페이지 매핑
    @GetMapping("/policy/location")
    public String location(){
        return "/policy/location";
    }
    // privacy (개인정보 처리 약관) 페이지 매핑
    @GetMapping("/policy/privacy")
    public String privacy(){
        return "/policy/privacy";
    }
}
