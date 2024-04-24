package kr.co.lotteon.controller;

import kr.co.lotteon.dto.admin.BannerDTO;
import kr.co.lotteon.dto.product.*;
import kr.co.lotteon.repository.product.Cate1Repository;
import kr.co.lotteon.service.admin.BannerService;
import kr.co.lotteon.service.product.CateService;
import kr.co.lotteon.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ProductController {

    private final ProductService productService;
    private final BannerService bannerService;
    // 상품 카테고리를 불러오기 위한 cateService
    private final CateService cateService;

    // cart 페이지 매핑
    @GetMapping("/product/cart")
    public String cart(){
        return "/product/cart";
    }



    // complete(주문 완료) 페이지 매핑
    @GetMapping("/product/complete")
    public String complete(){
        return "/product/complete";
    }

    // list (상품 목록) 페이지 매핑
    @GetMapping("/product/list")
    public String list(Model model, PageRequestDTO pageRequestDTO){

        // 상품 목록 조회
        PageResponseDTO pageResponseDTO = null;
        pageResponseDTO = productService.productList(pageRequestDTO);

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

    // order 페이지 매핑
    @GetMapping("/product/order")
    public String order(){
        return "/product/order";
    }

    // search (상품 검색) 페이지 매핑
    @GetMapping("/product/search")
    public String search(){
        return "/product/search";
    }

    // view (상품 상세 보기) 페이지 매핑
    @GetMapping("/product/view")
    public String view(Model model, ProductDTO productDTO){

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
        Map<String, List<String>> prodOptions = productService.selectProdOption(productDTO.getProdNo());

        log.info("prodOptions : " + prodOptions);

        // 옵션 맵 참조
        model.addAttribute("prodOptions", prodOptions);

        return "/product/view";
    }
}
