package kr.co.lotteon.controller;

import groovy.lang.Tuple;
import kr.co.lotteon.dto.admin.BannerDTO;
import kr.co.lotteon.dto.member.MemberDTO;
import kr.co.lotteon.dto.product.*;
import kr.co.lotteon.repository.product.Cate1Repository;
import kr.co.lotteon.service.admin.BannerService;
import kr.co.lotteon.service.member.MemberService;
import kr.co.lotteon.service.my.MyService;
import kr.co.lotteon.service.product.CartService;
import kr.co.lotteon.service.product.CateService;
import kr.co.lotteon.service.product.OptionService;
import kr.co.lotteon.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.Console;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ProductController {

    private final ProductService productService;
    private final BannerService bannerService;
    private final CartService cartService;
    // 상품 카테고리를 불러오기 위한 cateService
    private final CateService cateService;
    private final OptionService optionService;
    private final MemberService memberService;

    // cart 페이지 매핑
    @GetMapping("/product/cart")
    public String cart(@RequestParam("uid") String uid, Model model){
        
        // 회사별 출력을 위한 회사만 가져오기
        List<String> companies = cartService.selectCartCompany(uid);
        // 장바구니에 담긴 상품 정보 가져오기
        List<CartInfoDTO> cartProducts = cartService.selectCartProduct(uid);

        // 참조
        model.addAttribute("companies", companies);
        model.addAttribute("cartProducts", cartProducts);
        log.info("companies: {}", companies);
        log.info("cartProducts: {} ", cartProducts );

        return "/product/cart";
    }

    // complete(주문 완료) 페이지 매핑
    @GetMapping("/product/complete")
    public String complete(){
        return "/product/complete";
    }

    // list (상품 목록) 페이지 매핑
    @GetMapping("/product/list")
    public String list(Model model, PageRequestDTO pageRequestDTO, SearchPageRequestDTO searchPageRequestDTO){

        // 상품 목록 조회
        PageResponseDTO pageResponseDTO = null;
        SearchPageResponseDTO searchPageResponseDTO = null;

        if (searchPageRequestDTO.getSearchKeyword() != null) {
            // 검색 글 목록 조회
            searchPageResponseDTO = productService.searchProducts(searchPageRequestDTO);

        }else {
            pageResponseDTO = productService.productList(pageRequestDTO);
        }

        model.addAttribute("pageResponseDTO", pageResponseDTO);

        // 카테고리 불러오기
        String c1Name = cateService.getc1Name(pageRequestDTO.getCate1());
        String c2Name = cateService.getc2Name(pageRequestDTO.getCate1(), pageRequestDTO.getCate2());
        String c3Name = cateService.getc3Name(pageRequestDTO.getCate2(), pageRequestDTO.getCate3());
        log.info("c1Name : " + c1Name);
        log.info("c2Name : " + c2Name);
        log.info("c3Name : " + c3Name);

        // 카테 리스트 가져오기
        List<Cate1DTO> cate1DTOS = cateService.getCate1List();
        List<Cate2DTO> cate2DTOS = cateService.getCate2List();
        List<Cate3DTO> cate3DTOS = cateService.getCate3List();
        log.info("cate1DTOS : " + cate1DTOS);
        log.info("cate2DTOS : " + cate2DTOS);
        log.info("cate3DTOS : " + cate3DTOS);

        // list페이지에 사용하기 위해 참조
        //model.addAttribute(pageResponseDTO);
        model.addAttribute("c1Name",c1Name);
        model.addAttribute("c2Name",c2Name);
        model.addAttribute("c3Name",c3Name);

        // 카테 리스트 참조
        model.addAttribute("cate1DTOS", cate1DTOS);
        model.addAttribute("cate2DTOS", cate2DTOS);
        model.addAttribute("cate3DTOS", cate3DTOS);
        return "/product/list";
    }

    // order 페이지 (cart->order)
    @GetMapping("/order")
    public String order(@RequestParam String uid, Model model, @RequestParam int[] cartNo){

        List<ProductDTO> productDTOS = productService.selectOrderFromCart(cartNo);
        log.info("컨트롤러 : "+productDTOS);
        MemberDTO memberDTO =memberService.findByUid(uid);

       /* Map<String, List<ProductDTO>> orderProducts = new HashMap<>();
        for(ProductDTO productDTO : productDTOS){
            String company = productDTO.getCompany();
            List<ProductDTO> companyProducts = orderProducts.getOrDefault(company, new ArrayList<>());

            companyProducts.add(productDTO);

            orderProducts.put(company, companyProducts);
        }*/
        model.addAttribute("productDTOS", productDTOS);
        model.addAttribute("memberDTO", memberDTO);

        //log.info("맵:"+orderProducts);
        return "/product/order";
    }

   @GetMapping("/product/order")
    public String order(){
        return "/product/order";
    }

    // search (상품 검색) 페이지 매핑
    @GetMapping("/product/search")
    public String search(Model model, SearchPageRequestDTO searchPageRequestDTO) {

        String searchKeyword = searchPageRequestDTO.getSearchKeyword();
        log.info("검색 컨트롤러" + searchKeyword);

        if (searchKeyword != null && !searchKeyword.isEmpty()) {
            // 검색어가 존재하는 경우 상품 검색 실행
            SearchPageResponseDTO searchPageResponseDTO = productService.searchProducts(searchPageRequestDTO);
            model.addAttribute("searchPageResponseDTO", searchPageResponseDTO);
            log.info("searchPageResponseDTO : " + searchPageResponseDTO);
        }

        return "/product/search";
    }

    // view (상품 상세 보기) 페이지 매핑
    @GetMapping("/product/view")
    public String view(Model model, ProductDTO productDTO,ProductReviewPageRequestDTO productReviewPageRequestDTO){

        // hit + 1
        productService.updateProductHit(productDTO.getProdNo());
        ProductDTO prod = productService.selectByprodNo(productDTO.getProdNo());
        log.info("productDTO : " + prod.toString());
        // productDTO 참조
        model.addAttribute("productDTO", prod);

        // 카테 가져오기
        String c1Name = cateService.getc1Name(productDTO.getCate1());
        String c2Name = cateService.getc2Name(productDTO.getCate1(), productDTO.getCate2());
        String c3Name = cateService.getc3Name( productDTO.getCate2(), productDTO.getCate3());

        // 카테 참조
        model.addAttribute("c1Name",c1Name);
        model.addAttribute("c2Name",c2Name);
        model.addAttribute("c3Name",c3Name);

        // 카테 리스트 가져오기
        List<Cate1DTO> cate1DTOS = cateService.getCate1List();
        List<Cate2DTO> cate2DTOS = cateService.getCate2List();
        List<Cate3DTO> cate3DTOS = cateService.getCate3List();

        // 상품 상세보기 배너
        List<BannerDTO> prodBanners = bannerService.selectBanners("product");

        // 카테 리스트 참조
        model.addAttribute("cate1DTOS", cate1DTOS);
        model.addAttribute("cate2DTOS", cate2DTOS);
        model.addAttribute("cate3DTOS", cate3DTOS);

        // 배너 리스트 참조
        model.addAttribute("prodBanners", prodBanners);

        // 옵션 가져오기
        Map<String, List<String>> prodOptions = optionService.selectProdOption(productDTO.getProdNo());

        log.info("prodOptions : " + prodOptions);

        // 옵션 맵 참조
        model.addAttribute("prodOptions", prodOptions);

        // 옵션 네임 가져오기
        List<String> opNames = optionService.selectOpName(productDTO.getProdNo());
        log.info("opNames : " + opNames);
        model.addAttribute("opNames", opNames);

        // 리뷰 가져오기
        ProductReviewPageResponseDTO productReviewPageResponseDTO = productService.selectProductReview(productDTO.getProdNo(), productReviewPageRequestDTO);
        log.info("선택한 상품 : "+productDTO.getProdNo());
        log.info("테스트 : "+productService.selectProductReview(2,productReviewPageRequestDTO));
        log.info("선택한 상품의 리뷰들 "+productReviewPageResponseDTO);
        model.addAttribute("productReviewPageResponseDTO",productReviewPageResponseDTO);


        return "/product/view";
    }
}
