package kr.co.lotteon.controller;


import kr.co.lotteon.dto.admin.ArticleDTO;
import kr.co.lotteon.service.company.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Controller
public class CompanyController {

    private final CompanyService companyService;

    // 회사소개 매핑
    @GetMapping(value = {"/company", "/company/index"})
    public String index() {
      return ("/company/index");
    }
    // 채용 매핑
    @GetMapping(value = {"/company/recruit"})
    public String recruit() {
        return "/company/recruit";
    }
    // 미디어 매핑
    @GetMapping(value = { "/company/media"})
    public String media() {
        return "/company/media";
    }
    // 기업문화 매핑
    @GetMapping(value = { "/company/culture"})
    public String culture() {
        return "/company/culture";
    }
    // 소식과 이야기 매핑
    @GetMapping(value = { "/company/story"})
    public String story(Model model, @RequestParam("start") int start, @RequestParam("cate2") String cate2) {
         List<ArticleDTO> articles = companyService.selectStory(start, cate2);
        log.info("컨트롤러 : "+ articles);
         model.addAttribute("articles", articles);
        return "/company/story";
    }
}


