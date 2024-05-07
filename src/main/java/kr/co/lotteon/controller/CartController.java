package kr.co.lotteon.controller;

import kr.co.lotteon.dto.product.CartDTO;
import kr.co.lotteon.service.product.CartService;
import kr.co.lotteon.service.product.OptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class CartController {

    private final CartService cartService;
    private final OptionService optionService;

    // 카트에 답기
    @PostMapping("/cart/insert")
    public ResponseEntity<CartDTO> insertCart(@RequestBody CartDTO cartDTO){

        log.info("CartController" + cartDTO);
        cartService.insertCart(cartDTO);

        return ResponseEntity.ok(cartDTO);
    }

    // 카트의 정보 가져오기
    @GetMapping("/cart/opValue/{prodNo}/{opName}")
    public ResponseEntity<?> selectOpvalue(@PathVariable int prodNo, @PathVariable String opName){
        log.info("prodNo : " + prodNo + ", opName : " + opName);
        return optionService.selectOpDetail(prodNo, opName);
    }

    @ResponseBody
    @PostMapping("/cart/delete")
    public ResponseEntity<?> deleteCart(@RequestBody Map<String, int[]> cartNoData){
        int[] cartNoArray = cartNoData.get("cartNo");
        log.info("카트 삭제 Controller" + cartNoArray);
        cartService.deleteCart(cartNoArray);
        return ResponseEntity.ok().body(cartNoData);
    }

    @ResponseBody
    @PutMapping("/cart/count")
    public void countCart(@RequestBody CartDTO cartDTO){
        log.info("카트 수량 변경" + cartDTO);
        cartService.updateCartCount(cartDTO);
    }

}
