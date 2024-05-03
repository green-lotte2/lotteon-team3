package kr.co.lotteon.controller;


import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Log4j2
@Controller
public class CompanyController {
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
    public String story(Model model) {

        return "/company/story";
    }

}


