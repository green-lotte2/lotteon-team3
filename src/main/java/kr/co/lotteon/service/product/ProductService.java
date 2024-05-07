package kr.co.lotteon.service.product;

import com.querydsl.core.Tuple;
import kr.co.lotteon.dto.product.*;
import kr.co.lotteon.entity.product.Cart;
import kr.co.lotteon.entity.product.Option;
import kr.co.lotteon.entity.product.Product;
import kr.co.lotteon.entity.product.Review;
import kr.co.lotteon.mapper.ProductMapper;
import kr.co.lotteon.repository.product.OptionRepository;
import kr.co.lotteon.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor @Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;
    private final ProductMapper productMapper;
    private final ModelMapper modelMapper;

    private final SqlSession sqlSession;

    // 기본 상품 목록 조회
    public PageResponseDTO productList(PageRequestDTO pageRequestDTO){
        log.info("기본 상품 목록 조회 1" + pageRequestDTO);

        Pageable pageable = pageRequestDTO.getPageable();

        Page<Product> productsPage = productRepository.productList(pageRequestDTO, pageable);
        log.info("기본 상품 목록 조회 2" + productsPage);

        // Page<Product>를 List<ProductDTO>로 변환
        List<ProductDTO> productDTOS = productsPage.getContent().stream()
                .map(entity-> modelMapper.map(entity, ProductDTO.class))
                .toList();
        log.info("기본 상품 목록 조회 3" + productDTOS);

        int total = (int) productsPage.getTotalElements();

        return PageResponseDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(productDTOS)
                .total(total)
                .build();
    }

    // 상품 보기
    public ProductDTO selectByprodNo(int prodNo){
        Product product = productRepository.findById(prodNo).get();
        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
        return productDTO;
    }


    // 옵션 불러오기
    public Map<String, List<String>> selectProdOption(int prodNo){
        return optionRepository.selectProdOption(prodNo);
    }
 

    // 히트 올리기
    public void updateProductHit(int prodNo){
        productMapper.updateProductHit(prodNo);
    }

    // 상품 리뷰 조회
    public ProductReviewPageResponseDTO selectProductReview(int prodNo, ProductReviewPageRequestDTO productReviewPageRequestDTO) {

        log.info("상품 리뷰 목록 조회 1" + productReviewPageRequestDTO);
        Pageable pageable = productReviewPageRequestDTO.getPageable("rdate");
        Page<Tuple> tuples = productRepository.selectProductReview(prodNo, productReviewPageRequestDTO, pageable);
        log.info("상품 리뷰 목록 조회 2" + tuples.getContent());

        List<ReviewDTO> reviewDTOS = tuples.getContent().stream()
                .map(tuple -> {
                    Review review=tuple.get(0,Review.class);
                    String prodName=tuple.get(1,String.class);
                    String optionValue=tuple.get(2,String.class);

                    ReviewDTO reviewDTO=modelMapper.map(review,ReviewDTO.class);
                    reviewDTO.setProdName(prodName);
                    reviewDTO.setOptionValue(optionValue);
                    return reviewDTO;
                })
                .toList();
        log.info("상품 리뷰 목록 조회 3" + reviewDTOS);

        int total = (int) tuples.getTotalElements();

        return ProductReviewPageResponseDTO.builder()
                .productReviewPageRequestDTO(productReviewPageRequestDTO)
                .dtoList(reviewDTOS)
                .total(total)
                .build();
    }

    public PageResponseDTO searchProducts(PageRequestDTO pageRequestDTO){

        Pageable pageable = pageRequestDTO.getPageable();
        Page<Tuple> pageProduct = productRepository.searchProducts(pageRequestDTO, pageable);

        List<ProductDTO> dtoList = pageProduct.getContent().stream()
                .map(tuple ->
                        {
                            log.info("tuple : " + tuple);
                            Product product = tuple.get(0, Product.class);

                            log.info("product : " + product);

                            return modelMapper.map(product, ProductDTO.class);
                        }
                )
                .toList();

        int total = (int) pageProduct.getTotalElements();

        return PageResponseDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }



        // ========== 메인페이지 ==========
    // 최신상품
    public List<ProductDTO> bestProductMain(){return productRepository.bestProductMain();}
    public List<ProductDTO> recentProductMain(){return productRepository.recentProductMain();}
    public List<ProductDTO> discountProductMain(){return productRepository.discountProductMain();}
    public List<ProductDTO> hitProductMain(){return productRepository.hitProductMain();}
    public List<ProductDTO> recommendProductMain(){return productRepository.recommendProductMain();}
    // ==============================


}
