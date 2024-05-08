package kr.co.lotteon.controller;


import kr.co.lotteon.dto.admin.AdminPageRequestDTO;
import kr.co.lotteon.dto.admin.ArticleDTO;
import kr.co.lotteon.dto.company.RecruitDTO;
import kr.co.lotteon.dto.company.RecruitPageResponseDTO;
import kr.co.lotteon.dto.company.StoryPageRequestDTO;
import kr.co.lotteon.dto.company.StoryPageResponseDTO;
import kr.co.lotteon.service.company.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public String recruit(Model model, AdminPageRequestDTO adminPageRequestDTO, RecruitDTO recruitDTO){
        RecruitPageResponseDTO pageResponseDTO = companyService.selectRecruit(adminPageRequestDTO, recruitDTO);
        model.addAttribute("pageResponseDTO", pageResponseDTO);
        model.addAttribute("recruitDTO", recruitDTO);
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
    public String story(Model model, StoryPageRequestDTO storyPageRequestDTO) {
        StoryPageResponseDTO articles = companyService.selectStory(storyPageRequestDTO);
        log.info("컨트롤러 : "+ articles);
         model.addAttribute("articles", articles);
         model.addAttribute("cate2", storyPageRequestDTO.getCate2());
        return "/company/story";
    }
    // 소식과 이야기 더보기
    @GetMapping(value = { "/company/story/{pg}/{cate2}"})
    public ResponseEntity<?> story(StoryPageRequestDTO storyPageRequestDTO) {
        log.info("storyPageRequestDTO 더보기 : " + storyPageRequestDTO);
        StoryPageResponseDTO articles = companyService.selectStory(storyPageRequestDTO);
        log.info("더보기 컨트롤러 : "+ articles);

        return ResponseEntity.ok().body(articles.getDtoList());
    }
}


