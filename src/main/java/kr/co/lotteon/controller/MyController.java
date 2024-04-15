package kr.co.lotteon.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MyController {

    // my - home (마이페이지 메인) 페이지 매핑
    @GetMapping("/my/home")
    public String home(){
        return "/my/home";
    }
    // my - info (나의 설정) 페이지 매핑
    @GetMapping("/my/info")
    public String info(){
        return "/my/info";
    }
    // my - order (나의 전체 주문내역) 페이지 매핑
    @GetMapping("/my/order")
    public String order(){
        return "/my/order";
    }
    // my - point (나의 포인트) 페이지 매핑
    @GetMapping("/my/point")
    public String point(){
        return "/my/point";
    }
    // my - coupon 페이지 매핑
    @GetMapping("/my/coupon")
    public String coupon(){
        return "/my/coupon";
    }
    // my - qna (마이페이지 문의하기) 페이지 매핑
    @GetMapping("/my/qna")
    public String qna(){
        return "/my/qna";
    }
    // my - review (나의 리뷰내역) 페이지 매핑
    @GetMapping("/my/review")
    public String review(){
        return "/my/review";
    }
}
