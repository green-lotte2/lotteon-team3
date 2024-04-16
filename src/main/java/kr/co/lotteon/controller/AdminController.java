package kr.co.lotteon.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.lotteon.dto.product.Cate1DTO;
import kr.co.lotteon.dto.product.Cate2DTO;
import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.entity.product.Cate1;
import kr.co.lotteon.security.MyUserDetails;
import kr.co.lotteon.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AdminController {

    private final AdminService adminService;

    // admin index 페이지 매핑
    @GetMapping(value = {"/admin/","/admin/index"})
    public String admin(){
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
    public String list(){
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
    // 관리자 상품 등록 - cate1 선택 시 cate2 조회
    @GetMapping("/admin/product/register/{cate1}")
    @ResponseBody
    public ResponseEntity<?> registerCate2(@PathVariable int cate1){
        return adminService.findAllCate2ByCate1(cate1);
    }
    // 관리자 상품 등록 - DB insert
    @PostMapping("/admin/product/register")
    public String registerProduct(HttpServletRequest httpServletRequest,
                                  ProductDTO productDTO,
                                  @RequestParam("thumb190") MultipartFile thumb190,
                                  @RequestParam("thumb230") MultipartFile thumb230,
                                  @RequestParam("thumb456") MultipartFile thumb456,
                                  @RequestParam("detail860") MultipartFile detail860){
        productDTO.setIp(httpServletRequest.getRemoteAddr());

        // 현재 로그인 중인 사용자 정보 불러오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 로그인 중일 때 해당 사용자 id를 seller에 입력
        if (authentication != null && authentication.getPrincipal() instanceof MyUserDetails) {
            MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
            String sellerId = userDetails.getMember().getName();
            productDTO.setSeller(sellerId);
            // 로그인 상태가 아닐 때 == 개발 중 (배포시 삭제 할 것)
        } else if(authentication == null) {
            productDTO.setSeller("developer");
        }
        log.info("관리자 상품 등록 Cont " + productDTO);

        adminService.insertProduct(productDTO, thumb190, thumb230, thumb456, detail860);
        return "redirect:/admin/product/list";
    }
}
