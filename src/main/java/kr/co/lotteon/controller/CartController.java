package kr.co.lotteon.controller;

import kr.co.lotteon.dto.product.CartDTO;
import kr.co.lotteon.service.product.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CartController {

    private final CartService cartService;

    @PostMapping("/cart/insert")
    public ResponseEntity<CartDTO> insertCart(@RequestBody CartDTO cartDTO){

        log.info("CartController" + cartDTO);
        cartService.insertCart(cartDTO);

        return ResponseEntity.ok(cartDTO);
    }
}
