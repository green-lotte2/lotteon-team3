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
    public String notice() {
        return "/company/recruit";
    }

    @GetMapping(value = { "/company/media"})
    public String promote() {
        return "/company/media";
    }

    @GetMapping(value = { "/company/culture"})
    public String manage() {
        return "/company/culture";
    }



}


