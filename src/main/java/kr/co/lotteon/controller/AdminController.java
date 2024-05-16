package kr.co.lotteon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.lotteon.dto.admin.*;
import kr.co.lotteon.dto.company.RecruitDTO;
import kr.co.lotteon.dto.company.RecruitPageResponseDTO;
import kr.co.lotteon.dto.cs.BoardCateDTO;
import kr.co.lotteon.dto.cs.BoardDTO;
import kr.co.lotteon.dto.cs.BoardTypeDTO;
import kr.co.lotteon.dto.cs.CommentDTO;
import kr.co.lotteon.dto.product.*;
import kr.co.lotteon.entity.cs.Comment;
import kr.co.lotteon.entity.member.Terms;
import kr.co.lotteon.service.admin.AdminService;
import kr.co.lotteon.service.admin.BannerService;
import kr.co.lotteon.service.admin.company.AdminCompanyService;
import kr.co.lotteon.service.admin.cs.AdminBoardService;
import kr.co.lotteon.service.admin.cs.AdminCommentService;
import kr.co.lotteon.service.admin.SellerService;
import kr.co.lotteon.service.admin.member.AdminMemberService;
import kr.co.lotteon.service.admin.order.AdminOrderService;
import kr.co.lotteon.service.admin.product.AdminCateService;
import kr.co.lotteon.service.admin.product.AdminProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AdminController {

    private final AdminService adminService;
    private final BannerService bannerService;
    private final AdminCommentService adminCommentService;
    private final AdminProductService adminProductService;
    private final SellerService sellerService;
    private final AdminCateService adminCateService;
    private final AdminBoardService adminBoardService;
    private final AdminMemberService adminMemberService;
    private final AdminCompanyService adminCompanyService;
    private final AdminOrderService adminOrderService;

    private final ObjectMapper objectMapper;

    ////////////////  index  ///////////////////////////////////////////////////
    // admin index 페이지 매핑 + seller index 페이지 매핑
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
    // admin index 페이지 주문수 그래프 조회
    @GetMapping("/admin/orderChart")
    public ResponseEntity<?> orderChart() {
        List<Map<String, Object>> jsonResult = adminService.selectOrderForChart();
        log.info("페이지 그래프 조회 Cont 1: " + jsonResult);
        try {
            // 객체를 JSON으로 변환
            String json = objectMapper.writeValueAsString(jsonResult);
            // JSON 문자열을 ResponseEntity로 반환
            return ResponseEntity.ok().body(json);
        } catch (Exception e) {
            // JSON 변환에 실패한 경우
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("JSON 변환 오류");
        }
    }

    // admin index 페이지 가입자 그래프 조회
    @GetMapping("/admin/memberChart")
    public ResponseEntity<?> memberChart() {
        List<Map<String, Object>> jsonResult = adminService.selectMemberForChart();
        log.info("가입자 그래프 조회 Cont 1: " + jsonResult);
        try {
            // 객체를 JSON으로 변환
            String json = objectMapper.writeValueAsString(jsonResult);
            // JSON 문자열을 ResponseEntity로 반환
            return ResponseEntity.ok().body(json);
        } catch (Exception e) {
            // JSON 변환에 실패한 경우
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("JSON 변환 오류");
        }
    }
    ////////////////  banner  ///////////////////////////////////////////////////
    // config banner (관리자 배너 관리) 페이지 매핑
    @GetMapping("/admin/config/banner")
    public String banner(Model model){
        // 배너 cate 별 조회 ㅎㅎ;
        List<BannerDTO> mtBanner = bannerService.bannerList("main-top");
        List<BannerDTO> msBanner = bannerService.bannerList("main-slider");
        List<BannerDTO> pBanner = bannerService.bannerList("product");
        List<BannerDTO> lBanner = bannerService.bannerList("login");
        List<BannerDTO> myBanner = bannerService.bannerList("myPage");
        model.addAttribute("mtBanner", mtBanner);
        model.addAttribute("msBanner", msBanner);
        model.addAttribute("pBanner", pBanner);
        model.addAttribute("lBanner", lBanner);
        model.addAttribute("myBanner", myBanner);
        return "/admin/config/banner";
    }
    // config register (관리자 배너 등록) 전송
    @PostMapping("/admin/config/register")
    public String bannerRegister(@RequestParam("imgFile") MultipartFile imgFile, BannerDTO bannerDTO){
        log.info("관리자 배너 등록 Cont 1 : " + imgFile);
        log.info("관리자 배너 등록 Cont 2 : " + bannerDTO);
        bannerService.bannerRegister(imgFile, bannerDTO);
        return "redirect:/admin/config/banner";
    }

    // 배너 삭제
    @ResponseBody
    @PostMapping("/admin/banner/delete")
    public ResponseEntity<?> bannerDelete(@RequestBody Map<String, int[]> requestData){
        int[] bnoArray = requestData.get("bnoArray");
        log.info("배너 삭제 Cont 1 : " + requestData);
        return bannerService.bannerDelete(bnoArray);
    }

    // 배너 활성화 관리
    @GetMapping("/admin/banner/change/{bno}")
    public ResponseEntity<?> bannerActChange(@PathVariable("bno") int bno){
        return bannerService.bannerActChange(bno);
    }

    // config info (관리자 기본 환경 정보) 페이지 매핑
    @GetMapping("/admin/config/info")
    public String info(Model model){
        // 관리자 환경설정 기본환경 정보 - 약관 조회
        Terms terms = adminService.findByTerms();
        model.addAttribute("terms", terms);
        return "/admin/config/info";
    }
    ////////////////  product  ///////////////////////////////////////////////////
    // product list (관리자 상품 목록) 페이지 매핑
    @GetMapping("/admin/product/list")
    public String prodList(Model model, AdminProductPageRequestDTO adminProductPageRequestDTO){
        log.info("관리자 상품 목록 Cont 1 : " + adminProductPageRequestDTO);

        AdminProductPageResponseDTO adminPageResponseDTO = null;
        if(adminProductPageRequestDTO.getKeyword() == null) {
            // 일반 상품 목록 조회
            adminPageResponseDTO = adminProductService.adminSelectProducts(adminProductPageRequestDTO);
        }else {
            // 검색 상품 목록 조회
            log.info("키워드 검색 Cont" + adminProductPageRequestDTO.getKeyword());
            adminPageResponseDTO = adminProductService.adminSearchProducts(adminProductPageRequestDTO);
        }
        log.info("관리자 상품 목록 Cont 2 : " + adminPageResponseDTO);
        model.addAttribute("adminPageResponseDTO", adminPageResponseDTO);
        return "/admin/product/list";
    }

    // 관리자 상품 목록 검색 - cate1을 type으로 선택 시 cate1 조회
    @GetMapping("/admin/findCate1")
    @ResponseBody
    public ResponseEntity<?> findCate1s(){
        return adminProductService.findCate1s();
    }
    // 등록된 상품 보기
    @GetMapping("/admin/product/view")
    public String prodView(Model model, int prodNo){

        // 상품 상세 조회
        ProductDTO product = adminProductService.selectByprodNo(prodNo);
        log.info("판매자 상품 수정 Cont 1 : "+product);

        // Cate1 전체 조회
        List<Cate1DTO> cate1List = adminCateService.findAllCate1();
        log.info("판매자 상품 수정 Cont 2 : "+cate1List);

        // Cate2 조회
        List<Cate2DTO> cate2List = (List<Cate2DTO>) adminCateService.findAllCate2ByCate1(product.getCate1()).getBody();
        log.info("판매자 상품 수정 Cont 3 : "+cate2List);

        // Cate3 조회
        List<Cate3DTO> cate3List = (List<Cate3DTO>) adminCateService.findAllCate3ByCate2(product.getCate2()).getBody();
        log.info("판매자 상품 수정 Cont 4 : "+cate3List);

        // optionList 조회
        Map<String, List<Map<String, String>>> optionMap = adminProductService.selectProdOption(prodNo);
        log.info("optionList Map : "+optionMap);

        model.addAttribute("product", product);
        model.addAttribute("cate1List", cate1List);
        model.addAttribute("cate2List", cate2List);
        model.addAttribute("cate3List", cate3List);
        model.addAttribute("optionMap", optionMap);
        return "/admin/product/view";
    }

    // 관리자 상품 상태 변경
    @ResponseBody
    @GetMapping("/admin/product/status/{prodNo}/{statusNum}")
    public ResponseEntity<?> prodStatus(@PathVariable("prodNo")int prodNo, @PathVariable("statusNum")int statusNum){
        return adminProductService.prodStatUpdate(prodNo, statusNum);
    }
    // 관리자 상품 삭제
    @ResponseBody
    @PostMapping("/admin/product/delete")
    public ResponseEntity<?> prodDelete(@RequestBody List<Integer> prodNoList){
        log.info("상품 삭제 Cont 1 : " + prodNoList);
        return  adminProductService.prodDelete(prodNoList);
    }
    ////////////////  cs  ///////////////////////////////////////////////////
    // 관리자 게시판 목록 페이지 매핑
    @GetMapping("/admin/cs/list")
    public String boardList(Model model, String cate, AdminBoardPageRequestDTO adminBoardPageRequestDTO) {
        String group = adminBoardPageRequestDTO.getGroup();

        log.info("관리자 게시판 목록 Cont 1 : " + cate);
        // 게시글 조회
        AdminBoardPageResponseDTO adminBoardPageResponseDTO = adminBoardService.findBoardByGroup(cate, adminBoardPageRequestDTO);
        log.info("관리자 게시판 목록 Cont 2 : " +adminBoardPageResponseDTO);

        // 검색용 cate 조회
        List<BoardCateDTO> cates = adminBoardService.findBoardCate();

        model.addAttribute(adminBoardPageResponseDTO);
        model.addAttribute("group", group);
        model.addAttribute("cates", cates);
        return "/admin/cs/list";
    }
    // 관리자 게시글 쓰기 페이지 매핑
    @GetMapping("/admin/cs/write")
    public String boardDelete(Model model, @RequestParam("group") String group){
        log.info("관리자 게시글 등록 Cont 1 : " + group);
        // cate 조회
        List<BoardCateDTO> cates = adminBoardService.findBoardCate();
        model.addAttribute("group", group);
        model.addAttribute("cates", cates);
        return "/admin/cs/write";
    }
    // 관리자 게시글 Type(말머리) 조회
    @GetMapping("/admin/cs/type/{cate}")
    @ResponseBody
    public ResponseEntity<?> findTypes(@PathVariable("cate") String cate) {
        log.info("관리자 게시글 Type 조회 1 : " + cate);
        return adminBoardService.findBoardType(cate);
    }
    // 관리자 게시글 등록 POST
    @PostMapping("/admin/cs/write")
    public String adminBoardWrite(BoardDTO boardDTO){
        log.info("관리자 게시글 등록 Cont 1 : " +boardDTO);

        adminBoardService.adminBoardWrite(boardDTO);
        return "redirect:/admin/cs/list?group="+boardDTO.getGroup();
    }
    // 관리자 게시글 삭제 - list
    @ResponseBody
    @DeleteMapping("/admin/cs/delete/{bno}")
    public ResponseEntity<?> delete(@PathVariable("bno") int bno){
        log.info("관리자 게시글 삭제 Cont 1 : " + bno);
        return adminBoardService.boardDelete(bno);
    }
    // 관리자 게시글 삭제 - view
    @GetMapping("/admin/cs/delete")
    public String boardDelete(int bno, AdminBoardPageRequestDTO adminBoardPageRequestDTO){
        log.info("관리자 게시글 삭제 Cont 1 : " + bno);
        adminBoardService.boardDelete(bno);
        return "redirect:/admin/cs/list?group=" + adminBoardPageRequestDTO.getGroup() + "&pg=" + adminBoardPageRequestDTO.getPg();
    }
    // 관리자 게시판 보기 페이지 매핑
    @GetMapping("/admin/cs/view")
    public String boardView(Model model, String group, int bno, AdminBoardPageRequestDTO adminBoardPageRequestDTO){
        log.info("관리자 게시판 보기 Cont 1 : " + adminBoardPageRequestDTO);

        // 글 내용 조회
        BoardDTO board = adminBoardService.selectBoard(bno);
        // 답변 조회
        List<CommentDTO> comments = adminCommentService.commentList(bno);

        log.info("관리자 게시판 보기 Cont 2 : " + board);

        // pg, type, keyword 값
        AdminBoardPageResponseDTO adminBoardPageResponseDTO = AdminBoardPageResponseDTO.builder()
                .adminBoardPageRequestDTO(adminBoardPageRequestDTO)
                .build();
        log.info("관리자 게시판 보기 Cont 3 : " + adminBoardPageResponseDTO);

        model.addAttribute("group",group );
        model.addAttribute("board",board );
        model.addAttribute("comments",comments );
        model.addAttribute("adminBoardPageResponseDTO", adminBoardPageResponseDTO);
        return  "/admin/cs/view";
    }
    // 관리자 게시판 글 수정 페이지 매핑
    @GetMapping("/admin/cs/modify")
    public String boardModify(Model model, AdminBoardPageRequestDTO adminBoardPageRequestDTO, int bno){
        log.info("관리자 게시판 글 수정 Cont 1 : " + adminBoardPageRequestDTO);
        log.info("관리자 게시판 글 수정 Cont 2 : " + bno);

        BoardDTO board = adminBoardService.selectBoard(bno);
        log.info("관리자 게시판 글 수정 Cont 3 : " + board);

        // cate 조회
        List<BoardCateDTO> cates = adminBoardService.findBoardCate();

        // type 조회
        ResponseEntity<?> respType = adminBoardService.findBoardType(board.getCate());
        List<BoardTypeDTO> typeList = (List<BoardTypeDTO>) respType.getBody();

        // pg, type, keyword 값
        AdminBoardPageResponseDTO adminBoardPageResponseDTO = AdminBoardPageResponseDTO.builder()
                .adminBoardPageRequestDTO(adminBoardPageRequestDTO)
                .build();
        log.info("관리자 게시판 글 수정 Cont 4 : " + adminBoardPageResponseDTO);

        model.addAttribute("cates", cates);
        model.addAttribute("group",adminBoardPageRequestDTO.getGroup());
        model.addAttribute("board",board );
        model.addAttribute("typeList",typeList );
        model.addAttribute("adminBoardPageResponseDTO", adminBoardPageResponseDTO);
        return "/admin/cs/modify";
    }
    // 관리자 게시판 글 수정 전송
    @PostMapping("/admin/cs/modify")
    public String adminBoardModify(BoardDTO boardDTO){
        log.info("관리자 게시글 수정 Cont 1 : " +boardDTO);

        adminBoardService.adminBoardModify(boardDTO);
        return "redirect:/admin/cs/list?group="+boardDTO.getGroup();
    }
    ////////////////  comment  ///////////////////////////////////////////////////
    // 관리자 글 보기 답변 등록
    @PostMapping("/comment")
    public ResponseEntity<Comment> commentWrite(@RequestBody CommentDTO commentDTO) {
        log.info("commentWrite : " + commentDTO);

        ResponseEntity<Comment> commentResponseEntity = adminCommentService.insertComment(commentDTO);
        log.info("commentWrite ...2 : ");
        log.info(commentResponseEntity.getBody().toString());
        return commentResponseEntity;
    }

    // 관리자 글 보기 답변 삭제
    @DeleteMapping("/comment/{cno}")
    public ResponseEntity<?> deleteComment(@PathVariable("cno") int cno){
        return adminCommentService.deleteComment(cno);
    }
    // 관리자 글 보기 답변 수정
    @PutMapping("/comment")
    public ResponseEntity<?> modifyComment(@RequestBody CommentDTO commentDTO){
        log.info("modifyComment : " +commentDTO.toString());
        return adminCommentService.updateComment(commentDTO);
    }
    ////////////////  member  ///////////////////////////////////////////////////
    // 관리자 회원 현황 매핑
    @GetMapping("/admin/member/list")
    public String memberList(Model model, AdminPageRequestDTO adminPageRequestDTO){
        AdminMemberPageResponseDTO adminMemberPageResponseDTO = adminMemberService.selectMembers(adminPageRequestDTO);
        model.addAttribute("pageResponseDTO", adminMemberPageResponseDTO);
        return "/admin/member/list";
    }
    // 관리자 판매자 현황 매핑
    @GetMapping("/admin/member/seller")
    public String sellerList(Model model, AdminPageRequestDTO adminPageRequestDTO){
        AdminMemberPageResponseDTO adminMemberPageResponseDTO = adminMemberService.selectSellers(adminPageRequestDTO);
        model.addAttribute("pageResponseDTO", adminMemberPageResponseDTO);
        return "/admin/member/seller";
    }
    // 관리자 회원 삭제
    @GetMapping("/admin/member/delete/{uid}")
    public ResponseEntity<?> memberDelete(@PathVariable("uid") String uid){
        return adminMemberService.deleteMember(uid);
    }
    ////////////////  order  ///////////////////////////////////////////////////
    // 관리자 주문 현황 매핑
    @GetMapping("/admin/order/list")
    public String orderList(Model model, AdminPageRequestDTO adminPageRequestDTO){
        SellerOrderPageResponseDTO sellerOrderPageResponseDTO = null;
        if(adminPageRequestDTO.getKeyword() == null) {
            // 일반 주문 목록 조회
            sellerOrderPageResponseDTO = adminOrderService.selectOrderList(adminPageRequestDTO);
        }else {
            // 검색 주문 목록 조회 //////
            log.info("키워드 검색 Cont" + adminPageRequestDTO.getKeyword());
            sellerOrderPageResponseDTO = adminOrderService.searchOrderList(adminPageRequestDTO);
        }
        model.addAttribute("pageResponseDTO", sellerOrderPageResponseDTO);
        return "/admin/order/list";
    }
    ////////////////  company  ///////////////////////////////////////////////////
    // 관리자 스토리 매핑
    @GetMapping("/admin/company/story")
    public String storyList(Model model, AdminPageRequestDTO adminPageRequestDTO){
        AdminArticlePageResponseDTO pageResponseDTO = adminCompanyService.selectArticle("story", adminPageRequestDTO);
        model.addAttribute("pageResponseDTO", pageResponseDTO);
        return "/admin/company/story";
    }
    // 관리자 채용정보 매핑
    @GetMapping("/admin/company/recruit")
    public String recruitList(Model model, AdminPageRequestDTO adminPageRequestDTO, RecruitDTO recruitDTO){
        log.info("recruitDTO : " +recruitDTO);
        RecruitPageResponseDTO pageResponseDTO = adminCompanyService.selectRecruit(adminPageRequestDTO, recruitDTO);
        model.addAttribute("pageResponseDTO", pageResponseDTO);
        model.addAttribute("recruitDTO", recruitDTO);
        return "/admin/company/recruit";
    }
    // 관리자 회사소개 글쓰기 매핑
    @GetMapping("/admin/company/write")
    public String companyWrite(Model model, @RequestParam("cate1") String cate1){
        model.addAttribute("cate1", cate1);
        return "/admin/company/write";
    }
    // 채용 삭제
    @DeleteMapping(value = {"/admin/recruit/{rno}"})
    @ResponseBody
    public ResponseEntity<?> recruitDelete(@PathVariable("rno") int rno) {
        return adminCompanyService.recruitDelete(rno);
    }
    // 관리자 회사소개 - 채용 글쓰기 매핑
    @GetMapping("/admin/company/post")
    public String companyPost(){
        return "/admin/company/post";
    }
    // 관리자 회사소개 - 채용 수정
    @PostMapping("/admin/company/recruit")
    @ResponseBody
    public ResponseEntity<?> recruitModify(@RequestBody RecruitDTO recruitDTO){
        log.info("채용 수정 1 " + recruitDTO);
        return adminCompanyService.recruitUpdate(recruitDTO);
    }
    // 관리자 회사소개 글쓰기 전송
    @PostMapping("/admin/company/write")
    public String storyRegister(@RequestParam("thumb336") MultipartFile thumb336, ArticleDTO articleDTO){
        log.info("회사소개 글쓰기 Cont 1 : " + thumb336);
        log.info("회사소개 글쓰기Cont 2 : " + articleDTO);
        adminCompanyService.insertArticle(thumb336, articleDTO);
        return "redirect:/admin/company/"+articleDTO.getCate1();
    }
    // 관리자 회사소개 - 채용 글쓰기 전송
    @PostMapping("/admin/company/post")
    public String recruitPost(RecruitDTO recruitDTO){
        log.info("회사소개 글쓰기 - 채용 Cont 1 : " + recruitDTO);
        adminCompanyService.recruitPost(recruitDTO);
        return "redirect:/admin/company/recruit";
    }

   
    // 관리자 회사소개 글 수정 매핑
    @GetMapping("/admin/company/modify/{ano}")
    public String companyWrite(Model model, @PathVariable("ano") int ano){
        ArticleDTO article = adminCompanyService.selectArticle(ano);
        model.addAttribute("article", article);
        return "/admin/company/modify";
    }
    // 관리자 회사소개 글 수정 매핑
    @PostMapping("/admin/company/modify")
    public String companyWrite(ArticleDTO articleDTO){
        adminCompanyService.modifyArticle(articleDTO);
        return "redirect:/admin/company/" + articleDTO.getCate1();
    }

    // 관리자 회사소개 삭제
    @GetMapping("/admin/company/delete/{ano}")
    @ResponseBody
    public ResponseEntity<?> deleteArticle(@PathVariable("ano") int ano){

        return adminCompanyService.deleteArticle(ano);
    }
}
