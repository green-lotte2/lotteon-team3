package kr.co.lotteon.controller;

import kr.co.lotteon.dto.product.Cate1DTO;
import kr.co.lotteon.dto.product.Cate2DTO;
import kr.co.lotteon.entity.product.Cate1;
import kr.co.lotteon.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AdminController {

    private final AdminService adminService;

    // admin index 페이지 매핑
    @GetMapping(value = {"/admin/","/admin/index"})
    public String admin(){
        return "/admin/index";
    }
    // config banner (관리자 배너 관리) 페이지 매핑
    @GetMapping("/admin/config/banner")
    public String banner(){
        return "/admin/config/banner";
    }
    // config info (관리자 기본 환경 정보) 페이지 매핑
    @GetMapping("/admin/config/info")
    public String info(){
        return "/admin/config/info";
    }
    // product list (관리자 상품 목록) 페이지 매핑
    @GetMapping("/admin/product/list")
    public String list(){
        return "/admin/product/list";
    }
    // product register (관리자 상품 등록) 페이지 매핑
    @GetMapping("/admin/product/register")
    public String register(Model model){
        // Cate1 전체 조회
        List<Cate1DTO> cate1List = adminService.findAllCate1();
        log.info("관리자 상품 등록 Cont : "+cate1List);
        model.addAttribute("cate1List", cate1List);
        return "/admin/product/register";
    }
    // 관리자 상품 등록 - cate1 선택 시 cate2 조회
    @GetMapping("/admin/product/register/{cate1}")
    @ResponseBody
    public ResponseEntity<?> registerCate2(@PathVariable int cate1){
        return adminService.findAllCate2ByCate1(cate1);
    }
}
