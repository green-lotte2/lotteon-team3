package kr.co.lotteon.controller;

import kr.co.lotteon.config.AppInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j @Controller @RequiredArgsConstructor
public class MainController {

    private final AppInfo appInfo;

    // 메인페이지 매핑
    @GetMapping(value = {"/","/index"})
    public String index(Model model){

        model.addAttribute("appInfo", appInfo);
        return "/index";
    }

}
