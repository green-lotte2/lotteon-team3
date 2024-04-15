package kr.co.lotteon.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AdminController {

    // admin index 페이지 매핑
    @GetMapping(value = {"/admin","/admin/index"})
    public String admin(){
        return "/admin/index";
    }
    // product list (관리자 상품 목록) 페이지 매핑
    @GetMapping("/admin/product/list")
    public String list(){
        return "/admin/product/list";
    }
    // product register (관리자 상품 등록) 페이지 매핑
    @GetMapping("/admin/product/register")
    public String register(){
        return "/admin/product/register";
    }
}
