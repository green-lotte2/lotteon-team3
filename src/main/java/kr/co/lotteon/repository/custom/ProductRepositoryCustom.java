package kr.co.lotteon.repository.custom;

import com.querydsl.core.Tuple;
import kr.co.lotteon.dto.admin.AdminProductPageRequestDTO;
import kr.co.lotteon.dto.cs.CsPageRequestDTO;
import kr.co.lotteon.dto.product.*;
import kr.co.lotteon.entity.cs.BoardEntity;
import kr.co.lotteon.entity.product.Option;
import kr.co.lotteon.entity.product.Product;
import kr.co.lotteon.entity.product.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ProductRepositoryCustom {

    // 관리자 상품 목록 조회
    public Page<Product> adminSelectProducts(AdminProductPageRequestDTO adminProductPageRequestDTO, Pageable pageable);

    public Page<Product> adminSearchProducts(AdminProductPageRequestDTO adminProductPageRequestDTO, Pageable pageable);

    // 판매자 상품 목록 (Page) 조회 (where seller = 본인)
    public Page<Product> sellerSelectProducts(AdminProductPageRequestDTO adminProductPageRequestDTO, Pageable pageable, String sellerId);
    public Page<Product> sellerSearchProducts(AdminProductPageRequestDTO adminProductPageRequestDTO, Pageable pageable, String sellerId);

    // 판매자 상품 문의글 조회용 상품 (List) 조회 (where seller = 본인)
    public List<Integer> selectProdNoForQna(String sellerId);
    // 상품 목록 기본 리스트
    public Page<Product> productList(PageRequestDTO pageRequestDTO, Pageable pageable);


    // ========== 메인페이지 상품리스트 ==========
    
    // 베스트 상품
    public List<ProductDTO> bestProductMain();
    //인기 상품
    public List<ProductDTO> bestProductMain2();

    // 최신 상품
    public List<ProductDTO> recentProductMain();
    
    // 할인상품
    public List<ProductDTO> discountProductMain();
    
    // 히트상품
    public List<ProductDTO> hitProductMain();

    // 추천상품
    public List<ProductDTO> recommendProductMain();
// ===========================================
    
    // 마이페이지 리뷰내역 조회
    public Page<Tuple> memberSelectReview(String uid, ProductReviewPageRequestDTO productReviewPageRequestDTO, Pageable pageable);

    // 상품리뷰 조회
    Page<Tuple> selectProductReview(int prodNo, ProductReviewPageRequestDTO productReviewPageRequestDTO, Pageable pageable);

    // 메인 검색창
    Page<Tuple> searchProducts(SearchPageRequestDTO searchPageRequestDTO, Pageable pageable);

    // 상품명 검색창
    Page<Tuple> searchProductsProdName(SearchPageRequestDTO searchPageRequestDTO, Pageable pageable);

    // 상품설명 검색창
    Page<Tuple> searchProductsDescript(SearchPageRequestDTO searchPageRequestDTO, Pageable pageable);

    // 금액대설정 검색창
    Page<Tuple> searchProductsPrice(SearchPageRequestDTO searchPageRequestDTO, Pageable pageable, int min, int max);

    // 마이페이지 home 리뷰내역 최신순 5개 조회
    public List<Tuple> selectReviewByRdate(String uid);

}
