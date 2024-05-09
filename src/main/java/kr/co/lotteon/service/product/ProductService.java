package kr.co.lotteon.service.product;

import com.querydsl.core.Tuple;
import kr.co.lotteon.dto.product.*;
import kr.co.lotteon.entity.product.Cart;
import kr.co.lotteon.entity.product.Option;
import kr.co.lotteon.entity.product.Product;
import kr.co.lotteon.entity.product.Review;
import kr.co.lotteon.mapper.ProductMapper;
import kr.co.lotteon.repository.product.OptionRepository;
import kr.co.lotteon.repository.product.OrderRepository;
import kr.co.lotteon.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor @Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;
    private final ProductMapper productMapper;
    private final OrderRepository orderRepository;
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

    // 메인 검색(회사명, 상품명, 상품설명)
    public SearchPageResponseDTO searchProducts(SearchPageRequestDTO searchPageRequestDTO) {
        Page<Tuple> pageProduct = productRepository.searchProducts(searchPageRequestDTO, searchPageRequestDTO.getPageable());

        List<ProductDTO> dtoList = pageProduct.getContent().stream()
                .map(tuple -> {
                    // Tuple에서 필요한 데이터를 추출하여 ProductDTO 객체를 생성
                    ProductDTO productDTO = new ProductDTO();
                    productDTO.setProdNo(tuple.get(0, Integer.class)); // 상품 번호
                    productDTO.setProdName(tuple.get(1, String.class)); // 상품명
                    productDTO.setDiscount(tuple.get(2, Integer.class)); // 상품할인율
                    productDTO.setPrice(tuple.get(3, Integer.class)); // 상품가격
                    productDTO.setSeller(tuple.get(4, String.class)); // 상품판매자
                    // 나머지 필드도 마찬가지로 설정

                    return productDTO;
                })
                .collect(Collectors.toList());

        int total = (int) pageProduct.getTotalElements();

        return SearchPageResponseDTO.builder()
                .searchPageRequestDTO(searchPageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    // 타입 검색(상품명)
    public SearchPageResponseDTO searchProductsProdName(SearchPageRequestDTO searchPageRequestDTO) {
        Page<Tuple> pageProduct = productRepository.searchProductsProdName(searchPageRequestDTO, searchPageRequestDTO.getPageable());

        List<ProductDTO> dtoList = pageProduct.getContent().stream()
                .map(tuple -> {
                    // Tuple에서 필요한 데이터를 추출하여 ProductDTO 객체를 생성
                    ProductDTO productDTO = new ProductDTO();
                    productDTO.setProdNo(tuple.get(0, Integer.class)); // 상품 번호
                    productDTO.setProdName(tuple.get(1, String.class)); // 상품명
                    productDTO.setDiscount(tuple.get(2, Integer.class)); // 상품할인율
                    productDTO.setPrice(tuple.get(3, Integer.class)); // 상품가격
                    productDTO.setSeller(tuple.get(4, String.class)); // 상품판매자
                    // 나머지 필드도 마찬가지로 설정

                    return productDTO;
                })
                .collect(Collectors.toList());

        int total = (int) pageProduct.getTotalElements();

        return SearchPageResponseDTO.builder()
                .searchPageRequestDTO(searchPageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    // 타입 검색(상품설명)
    public SearchPageResponseDTO searchProductsDescript(SearchPageRequestDTO searchPageRequestDTO) {
        Page<Tuple> pageProduct = productRepository.searchProductsDescript(searchPageRequestDTO, searchPageRequestDTO.getPageable());

        List<ProductDTO> dtoList = pageProduct.getContent().stream()
                .map(tuple -> {
                    // Tuple에서 필요한 데이터를 추출하여 ProductDTO 객체를 생성
                    ProductDTO productDTO = new ProductDTO();
                    productDTO.setProdNo(tuple.get(0, Integer.class)); // 상품 번호
                    productDTO.setProdName(tuple.get(1, String.class)); // 상품명
                    productDTO.setDiscount(tuple.get(2, Integer.class)); // 상품할인율
                    productDTO.setPrice(tuple.get(3, Integer.class)); // 상품가격
                    productDTO.setSeller(tuple.get(4, String.class)); // 상품판매자
                    // 나머지 필드도 마찬가지로 설정

                    return productDTO;
                })
                .collect(Collectors.toList());

        int total = (int) pageProduct.getTotalElements();

        return SearchPageResponseDTO.builder()
                .searchPageRequestDTO(searchPageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    // 타입 검색(상품 가격대)
    public SearchPageResponseDTO searchProductsPrice(SearchPageRequestDTO searchPageRequestDTO, int min, int max) {
        log.info("서비스...1" + min);
        log.info("서비스...1" + max);
        log.info("서비스...1" + searchPageRequestDTO.getSearchType());

        Page<Tuple> pageProduct = productRepository.searchProductsPrice(searchPageRequestDTO, searchPageRequestDTO.getPageable(), min, max);

        log.info("서비스...2"+pageProduct);

        List<ProductDTO> dtoList = pageProduct.getContent().stream()
                .map(tuple -> {
                    // Tuple에서 필요한 데이터를 추출하여 ProductDTO 객체를 생성
                    ProductDTO productDTO = new ProductDTO();
                    productDTO.setProdNo(tuple.get(0, Integer.class)); // 상품 번호
                    productDTO.setProdName(tuple.get(1, String.class)); // 상품명
                    productDTO.setDiscount(tuple.get(2, Integer.class)); // 상품할인율
                    productDTO.setPrice(tuple.get(3, Integer.class)); // 상품가격
                    productDTO.setSeller(tuple.get(4, String.class)); // 상품판매자
                    // 나머지 필드도 마찬가지로 설정

                    log.info("서비스...3"+productDTO);
                    return productDTO;
                })
                .collect(Collectors.toList());

        int total = (int) pageProduct.getTotalElements();

        log.info("서비스...4"+total);

        SearchPageResponseDTO resultt = SearchPageResponseDTO.builder()
                .searchPageRequestDTO(searchPageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
        log.info("서비스...5"+resultt);

        return SearchPageResponseDTO.builder()
                .searchPageRequestDTO(searchPageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    // 오더 페이지
    public List<ProductDTO> selectOrderFromCart(int[] cartNo){

        log.info("오더 조회 서비스 1" + Arrays.toString(cartNo));
        List<ProductDTO> productDTOS = new ArrayList<>();
        for(int cartN : cartNo){
            List<Tuple> result = orderRepository.selectOrderFromCart(cartN);
            log.info("오더 조회 서비스 2"+result);

            // mappedProductDTOs에 for문 돌린 List 저장
            List<ProductDTO> mappedProductDTOs = result.stream()
                    .map(tuple ->{
                        int count = tuple.get(0, Integer.class);
                        Product product = tuple.get(1, Product.class);

                        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
                        productDTO.setCount(count);
                        return productDTO;
                    })
                    .collect(Collectors.toList());
            log.info("오더 조회 서비스 3"+mappedProductDTOs);
            
            // 선언해둔 productDTOS에 모두 덮어씌우기
            productDTOS.addAll(mappedProductDTOs);
        }
        return productDTOS;
    }

    public ProductDTO prodToOrder(int prodNo){
        Product result = productRepository.findById(prodNo).get();

        return modelMapper.map(result, ProductDTO.class);
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
