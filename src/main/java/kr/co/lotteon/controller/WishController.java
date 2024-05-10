package kr.co.lotteon.controller;

import kr.co.lotteon.dto.product.WishPageRequestDTO;
import kr.co.lotteon.dto.product.WishPageResponseDTO;
import kr.co.lotteon.service.product.WishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@RequiredArgsConstructor
@Controller
public class WishController {

    private final WishService wishService;

    // wish 페이지 매핑
    @GetMapping("/my/wish")
    public String wishList(Model model, WishPageRequestDTO wishPageRequestDTO){
        WishPageResponseDTO pageResponseDTO = wishService.selectWishList(wishPageRequestDTO);
        model.addAttribute("pageResponseDTO", pageResponseDTO);
        return "/my/wish";
    }

    // 찜하기
    @GetMapping("/wish/{prodNo}/{wish}")
    @ResponseBody
    public ResponseEntity<?> wishChange(@PathVariable("prodNo") int prodNo, @PathVariable("wish") int wish){
        return wishService.changeWish(prodNo, wish);
    }
}
