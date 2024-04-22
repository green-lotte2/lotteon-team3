package kr.co.lotteon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import kr.co.lotteon.dto.admin.AdminBoardPageRequestDTO;
import kr.co.lotteon.dto.admin.AdminBoardPageResponseDTO;
import kr.co.lotteon.dto.admin.AdminProductPageRequestDTO;
import kr.co.lotteon.dto.admin.AdminProductPageResponseDTO;
import kr.co.lotteon.dto.cs.BoardDTO;
import kr.co.lotteon.dto.product.*;
import kr.co.lotteon.security.MyUserDetails;
import kr.co.lotteon.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.objectweb.asm.TypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AdminController {

    private final AdminService adminService;

    // admin index 페이지 매핑
    @GetMapping(value = {"/admin","/admin/index"})
    public String admin(Model model){
        // 공지사항 조회
        List<BoardDTO> noticeList = adminService.adminSelectNotices();
        // 고객문의 조회
        List<BoardDTO> qnaList = adminService.adminSelectQnas();
        model.addAttribute("noticeList", noticeList);
        model.addAttribute("qnaList", qnaList);
        return "/admin/index";
    }
    // config banner (관리자 배너 관리) 페이지 매핑
    @GetMapping("/admin/config/banner")
    public String banner(){
        return "/admin/config/banner";
    }
    // config info (관리자 기본 환경 정보) 페이지 매핑
    @GetMapping("/admin/config/info")
    public String info(){
        return "/admin/config/info";
    }
    // product list (관리자 상품 목록) 페이지 매핑
    @GetMapping("/admin/product/list")
    public String prodList(Model model, AdminProductPageRequestDTO adminProductPageRequestDTO){
        log.info("관리자 상품 목록 Cont 1 : " + adminProductPageRequestDTO);

        AdminProductPageResponseDTO adminPageResponseDTO = null;
        if(adminProductPageRequestDTO.getKeyword() == null) {
            // 일반 상품 목록 조회
            adminPageResponseDTO = adminService.adminSelectProducts(adminProductPageRequestDTO);
        }else {
            // 검색 상품 목록 조회
            log.info("키워드 검색 Cont" + adminProductPageRequestDTO.getKeyword());
            adminPageResponseDTO = adminService.adminSearchProducts(adminProductPageRequestDTO);
        }
        log.info("관리자 상품 목록 Cont 2 : " + adminPageResponseDTO);
        model.addAttribute("adminPageResponseDTO", adminPageResponseDTO);
        return "/admin/product/list";
    }
    // product register (관리자 상품 등록) 페이지 매핑
    @GetMapping("/admin/product/register")
    public String register(Model model){
        // Cate1 전체 조회
        List<Cate1DTO> cate1List = adminService.findAllCate1();
        log.info("관리자 상품 등록 Cont : "+cate1List);
        model.addAttribute("cate1List", cate1List);
        return "/admin/product/register";
    }

    // 관리자 상품 목록 검색 - cate1을 type으로 선택 시 cate1 조회
    @GetMapping("/admin/findCate1")
    @ResponseBody
    public ResponseEntity<?> findCate1s(){
        return adminService.findCate1s();
    }

    // 관리자 상품 등록 - cate1 선택 시 cate2 조회
    @GetMapping("/admin/product/register/{cate1}")
    @ResponseBody
    public ResponseEntity<?> registerCate2(@PathVariable int cate1){
        return adminService.findAllCate2ByCate1(cate1);
    }
    // 관리자 상품 등록 - cate2 선택 시 cate3 조회
    @GetMapping("/admin/product/cate3/{cate2}")
    @ResponseBody
    public ResponseEntity<?> registerCate3(@PathVariable int cate2){
        return adminService.findAllCate3ByCate2(cate2);
    }
    // 관리자 상품 등록 - DB insert
    @RequestMapping(value = "/admin/product/register", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public String registerProduct(HttpServletRequest httpServletRequest,
                                  ProductDTO productDTO,
                                  @RequestParam("optionDTOList") String optionDTOListJson,
                                  @RequestParam("thumb190") MultipartFile thumb190,
                                  @RequestParam("thumb230") MultipartFile thumb230,
                                  @RequestParam("thumb456") MultipartFile thumb456,
                                  @RequestParam("detail860") MultipartFile detail860){
        productDTO.setIp(httpServletRequest.getRemoteAddr());
        log.info("관리자 상품 등록 Cont 1 " + optionDTOListJson);


        // 현재 로그인 중인 사용자 정보 불러오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 로그인 중일 때 해당 사용자 id를 seller에 입력
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof MyUserDetails) {
            MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
            String sellerId = userDetails.getMember().getName();
            productDTO.setSeller(sellerId);
            log.info("관리자 상품 등록 Cont 1 " + productDTO);
            // 로그인 상태가 아닐 때 == 개발 중 (배포시 삭제 할 것)
        } else if(authentication == null) {
            productDTO.setSeller("developer");
            log.info("관리자 상품 등록 Cont 2 " + productDTO);
        }
        log.info("관리자 상품 등록 Cont " + productDTO);

        adminService.insertProduct(optionDTOListJson, productDTO, thumb190, thumb230, thumb456, detail860);


        return "redirect:/admin/product/list";
    }

    // 상품 삭제
    @ResponseBody
    @PostMapping("/admin/product/delete")
    public ResponseEntity prodDelete(@RequestBody Map<String, int[]> requestData){
        int[] prodNoArray = requestData.get("prodNoArray");
        log.info("상품 삭제 Cont 1 : " + requestData);
        return adminService.prodDelete(prodNoArray);
    }
    // 관리자 게시판 목록 페이지 매핑
    @GetMapping("/admin/cs/list")
    public String boardList(Model model, AdminBoardPageRequestDTO adminBoardPageRequestDTO) {

        AdminBoardPageResponseDTO adminBoardPageResponseDTO = adminService.findBoardByGroup(adminBoardPageRequestDTO);
        log.info("관리자 게시판 목록 Cont : " +adminBoardPageResponseDTO);
        model.addAttribute(adminBoardPageResponseDTO);
        model.addAttribute("group", adminBoardPageRequestDTO.getGroup());
        return "/admin/cs/list";
    }
    // 관리자 게시글 등록 페이지 매핑
    @GetMapping("/admin/cs/register")
    public String boardDelete(Model model, @RequestParam("group") String group){
        log.info("관리자 게시글 등록 Cont 1 : " + group);
        model.addAttribute("group", group);
        return "/admin/cs/register";
    }
    // 관리자 게시글 삭제
    @DeleteMapping("/admin/cs/delete/{bno}")
    public ResponseEntity<?> boardDelete(@PathVariable("bno") int bno){
        log.info("관리자 게시글 삭제 Cont 1 : " + bno);
        return adminService.boardDelete(bno);
    }
    // 관리자 게시판 보기 페이지 매핑
    @GetMapping("/admin/cs/view")
    public String boardView(Model model, int bno, AdminBoardPageRequestDTO adminBoardPageRequestDTO){
        log.info("관리자 게시판 보기 Cont 1 : " + adminBoardPageRequestDTO);
        BoardDTO board = adminService.selectBoard(bno);
        log.info("관리자 게시판 보기 Cont 2 : " + board);
        // pg, type, keyword 값
        AdminBoardPageResponseDTO adminBoardPageResponseDTO = AdminBoardPageResponseDTO.builder()
                .adminBoardPageRequestDTO(adminBoardPageRequestDTO)
                .build();
        log.info("관리자 게시판 보기 Cont 3 : " + adminBoardPageResponseDTO);
        model.addAttribute("board",board );
        model.addAttribute("adminBoardPageResponseDTO", adminBoardPageResponseDTO);
        return  "/admin/cs/view";
    }
}
