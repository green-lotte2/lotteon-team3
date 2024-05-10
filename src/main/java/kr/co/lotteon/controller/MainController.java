package kr.co.lotteon.controller;

import kr.co.lotteon.config.AppInfo;
import kr.co.lotteon.dto.admin.BannerDTO;
import kr.co.lotteon.dto.product.*;
import kr.co.lotteon.service.admin.BannerService;
import kr.co.lotteon.service.product.CateService;
import kr.co.lotteon.service.product.ProductService;
import kr.co.lotteon.service.product.WishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j @Controller @RequiredArgsConstructor @EnableCaching
public class MainController {

    private final CateService cateService;
    private final ProductService productService;
    private final BannerService bannerService;
    private final WishService wishService;
    
    // 메인페이지 매핑
    @GetMapping(value = {"/","/index"})
    public String index(Model model){
        log.info("index...");
        List<Cate1DTO> cate1DTOS = cateService.getCate1List();
        List<Cate2DTO> cate2DTOS = cateService.getCate2List();
        List<Cate3DTO> cate3DTOS = cateService.getCate3List();

        log.info("cate1DTOS : " + cate1DTOS);
        log.info("cate2DTOS : " + cate2DTOS);
        log.info("cate3DTOS : " + cate3DTOS);

        model.addAttribute("cate1DTOS", cate1DTOS);
        model.addAttribute("cate2DTOS", cate2DTOS);
        model.addAttribute("cate3DTOS", cate3DTOS);

        // 베스트 상품
        List<ProductDTO> bestProducts = productService.bestProductMain();
        log.info("베스트 상품 : " + bestProducts);
        // 히트상품
        List<ProductDTO> hitProducts = productService.hitProductMain();
        log.info("히트상품 : " + hitProducts);
        // 추천상품
        List<ProductDTO> recommendProducts = productService.recommendProductMain();
        log.info("추천상품 : " + recommendProducts);
        // 최신상품
        List<ProductDTO> recentProducts = productService.recentProductMain();
        log.info("최신상품 : " + recentProducts);
        // 할인상품
        List<ProductDTO> discountProducts = productService.discountProductMain();
        log.info("할인 : " + discountProducts);


        // 메인 - 상단 배너
        List<BannerDTO> topBanners = bannerService.selectBanners("main-top");
        // 메인 - 슬라이더 배너
        List<BannerDTO> sdBanners = bannerService.selectBanners("main-slider");

        // ==== 참조 ====
        // 베스트 상품
        model.addAttribute("bestProducts",bestProducts);
        // 히트상품
        model.addAttribute("hitProducts",hitProducts);
        // 추천상품
        model.addAttribute("recommendProducts",recommendProducts);
        // 최신상품
        model.addAttribute("recentProducts",recentProducts);
        // 할인상품
        model.addAttribute("discountProducts",discountProducts);

        // 메인 - 상단 배너
        model.addAttribute("topBanners",topBanners);
        log.info("탑배너 : "+topBanners);
        // 메인 - 슬라이더 배너
        model.addAttribute("sdBanners",sdBanners);

        return "/index";
    }

}
