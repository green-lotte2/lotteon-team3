package kr.co.lotteon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.lotteon.dto.cs.BoardDTO;
import kr.co.lotteon.service.admin.AdminService;
import kr.co.lotteon.service.admin.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class SellerController {

    private final AdminService adminService;
    private final ObjectMapper objectMapper;

    // seller index 페이지 매핑
    @GetMapping(value = {"/seller","/seller/index"})
    public String seller(Model model){
        // 공지사항 조회
        List<BoardDTO> noticeList = adminService.adminSelectNotices();
        // 고객문의 조회
        List<BoardDTO> qnaList = adminService.adminSelectQnas();
        model.addAttribute("noticeList", noticeList);
        model.addAttribute("qnaList", qnaList);
        return "/seller/index";
    }
}
