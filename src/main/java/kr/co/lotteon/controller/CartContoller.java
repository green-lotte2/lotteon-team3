package kr.co.lotteon.controller;

import kr.co.lotteon.dto.product.CartDTO;
import kr.co.lotteon.service.product.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CartContoller {

    private final CartService cartService;

    @PostMapping("/product/cart")
    public String insertCart(@RequestBody CartDTO cartDTO){
        String uid = cartDTO.getUid();
        int ProdNo = cartDTO.getProdNo();
        int count = cartDTO.getCount();

        return "/product/cart";
    }
}
