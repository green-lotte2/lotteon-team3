package kr.co.lotteon.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ProductController {

    // cart 페이지 매핑
    @GetMapping("/product/cart")
    public String cart(){
        return "/product/cart";
    }
    // complete(주문 완료) 페이지 매핑
    @GetMapping("/product/complete")
    public String complete(){
        return "/product/complete";
    }
    // list (상품 목록) 페이지 매핑
    @GetMapping("/product/list")
    public String list(){
        return "/product/list";
    }
    // order 페이지 매핑
    @GetMapping("/product/order")
    public String order(){
        return "/product/order";
    }
    // search (상품 검색) 페이지 매핑
    @GetMapping("/product/search")
    public String search(){
        return "/product/search";
    }
    // view (상품 상세 보기) 페이지 매핑
    @GetMapping("/product/view")
    public String view(){
        return "/product/view";
    }
}
