package kr.co.lotteon.controller;


import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Log4j2
@Controller
public class CompanyController {

    @GetMapping(value = {"/company", "/company/index"})
    public String index() {
      return ("/company/index");
    }

    @GetMapping(value = {"/company/recruit"})
    public String recruit() {
        return "/company/recruit";
    }

    @GetMapping(value = { "/company/media"})
    public String media() {
        return "/company/media";
    }

    @GetMapping(value = { "/company/culture"})
    public String culture() {
        return "/company/culture";
    }

    @GetMapping(value = { "/company/story"})
    public String story() {
        return "/company/story";
    }



}


