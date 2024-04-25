package kr.co.lotteon.controller;

import kr.co.lotteon.dto.product.CartDTO;
import kr.co.lotteon.service.product.CartService;
import kr.co.lotteon.service.product.OptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CartController {

    private final CartService cartService;
    private final OptionService optionService;

    @PostMapping("/cart/insert")
    public ResponseEntity<CartDTO> insertCart(@RequestBody CartDTO cartDTO){

        log.info("CartController" + cartDTO);
        cartService.insertCart(cartDTO);

        return ResponseEntity.ok(cartDTO);
    }

    @GetMapping("/cart/opValue/{prodNo}/{opName}")
    public ResponseEntity<?> selectOpvalue(@PathVariable int prodNo, @PathVariable String opName){
        log.info("prodNo : " + prodNo + ", opName : " + opName);
        return optionService.selectOpDetail(prodNo, opName);
    }
}
