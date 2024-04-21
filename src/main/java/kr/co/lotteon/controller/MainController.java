package kr.co.lotteon.controller;

import kr.co.lotteon.config.AppInfo;
import kr.co.lotteon.dto.product.Cate1DTO;
import kr.co.lotteon.dto.product.Cate2DTO;
import kr.co.lotteon.dto.product.Cate3DTO;
import kr.co.lotteon.service.product.CateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j @Controller @RequiredArgsConstructor
public class MainController {

    private final CateService cateService;

    // 메인페이지 매핑
    @GetMapping(value = {"/","/index"})
    public String index(Model model){
        List<Cate1DTO> cate1DTOS = cateService.getCate1List();
        List<Cate2DTO> cate2DTOS = cateService.getCate2List();
        List<Cate3DTO> cate3DTOS = cateService.getCate3List();

        log.info("cate1DTOS : " + cate1DTOS);
        log.info("cate2DTOS : " + cate2DTOS);
        log.info("cate3DTOS : " + cate3DTOS);

        model.addAttribute("cate1DTOS", cate1DTOS);
        model.addAttribute("cate2DTOS", cate2DTOS);
        model.addAttribute("cate3DTOS", cate3DTOS);

        return "/index";
    }

}
