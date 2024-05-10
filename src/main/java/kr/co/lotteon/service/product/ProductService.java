package kr.co.lotteon.service.product;

import com.querydsl.core.Tuple;
import jakarta.transaction.Transactional;
import kr.co.lotteon.dto.product.*;
import kr.co.lotteon.entity.product.*;
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
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
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

        Optional<Integer> cate1 = Optional.ofNullable(pageRequestDTO.getCate1());
        Optional<Integer> cate2 = Optional.ofNullable(pageRequestDTO.getCate2());
        Optional<Integer> cate3 = Optional.ofNullable(pageRequestDTO.getCate3());

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

    // 메인 검색
    public SearchPageResponseDTO searchProducts(SearchPageRequestDTO searchPageRequestDTO) {
        Page<Tuple> pageProduct = productRepository.searchProducts(searchPageRequestDTO, searchPageRequestDTO.getPageable());

        List<ProductDTO> dtoList = pageProduct.getContent().stream()
                .map(tuple -> {
                    // Tuple에서 필요한 데이터를 추출하여 ProductDTO 객체를 생성
                    ProductDTO productDTO = new ProductDTO();
                    productDTO.setProdNo(tuple.get(0, Integer.class)); // 상품 번호
                    productDTO.setProdName(tuple.get(1, String.class)); // 상품명
                    productDTO.setDescript(tuple.get(2, String.class)); // 상품설명
                    productDTO.setDiscount(tuple.get(3, Integer.class)); // 상품할인율
                    productDTO.setPrice(tuple.get(4, Integer.class)); // 상품가격
                    productDTO.setSeller(tuple.get(5, String.class)); // 상품판매자
                    productDTO.setDelivery(tuple.get(6, Integer.class)); // 배송
                    productDTO.setThumb1(tuple.get(7, String.class)); // 썸네일1
                    productDTO.setSold(tuple.get(8, Integer.class)); // 판매량
                    productDTO.setScore(tuple.get(9, Integer.class)); // 평점
                    productDTO.setReview(tuple.get(10, Integer.class)); // 후기
                    productDTO.setRdate(tuple.get(11, LocalDateTime.class)); // 등록날짜
                    productDTO.setCate1(tuple.get(12, Integer.class)); // cate1
                    productDTO.setCate2(tuple.get(13, Integer.class)); // cate2
                    productDTO.setCate3(tuple.get(14, Integer.class)); // cate3

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
                    productDTO.setDescript(tuple.get(2, String.class)); // 상품설명
                    productDTO.setDiscount(tuple.get(3, Integer.class)); // 상품할인율
                    productDTO.setPrice(tuple.get(4, Integer.class)); // 상품가격
                    productDTO.setSeller(tuple.get(5, String.class)); // 상품판매자
                    productDTO.setDelivery(tuple.get(6, Integer.class)); // 배송
                    productDTO.setThumb1(tuple.get(7, String.class)); // 썸네일1
                    productDTO.setSold(tuple.get(8, Integer.class)); // 판매량
                    productDTO.setScore(tuple.get(9, Integer.class)); // 평점
                    productDTO.setReview(tuple.get(10, Integer.class)); // 후기
                    productDTO.setRdate(tuple.get(11, LocalDateTime.class)); // 등록날짜
                    productDTO.setCate1(tuple.get(12, Integer.class)); // cate1
                    productDTO.setCate2(tuple.get(13, Integer.class)); // cate2
                    productDTO.setCate3(tuple.get(14, Integer.class)); // cate3

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
                    productDTO.setDescript(tuple.get(2, String.class)); // 상품설명
                    productDTO.setDiscount(tuple.get(3, Integer.class)); // 상품할인율
                    productDTO.setPrice(tuple.get(4, Integer.class)); // 상품가격
                    productDTO.setSeller(tuple.get(5, String.class)); // 상품판매자
                    productDTO.setDelivery(tuple.get(6, Integer.class)); // 배송
                    productDTO.setThumb1(tuple.get(7, String.class)); // 썸네일1
                    productDTO.setSold(tuple.get(8, Integer.class)); // 판매량
                    productDTO.setScore(tuple.get(9, Integer.class)); // 평점
                    productDTO.setReview(tuple.get(10, Integer.class)); // 후기
                    productDTO.setRdate(tuple.get(11, LocalDateTime.class)); // 등록날짜
                    productDTO.setCate1(tuple.get(12, Integer.class)); // cate1
                    productDTO.setCate2(tuple.get(13, Integer.class)); // cate2
                    productDTO.setCate3(tuple.get(14, Integer.class)); // cate3

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

        Page<Tuple> pageProduct = productRepository.searchProductsPrice(searchPageRequestDTO, searchPageRequestDTO.getPageable(), min, max);

        List<ProductDTO> dtoList = pageProduct.getContent().stream()
                .map(tuple -> {
                    // Tuple에서 필요한 데이터를 추출하여 ProductDTO 객체를 생성
                    ProductDTO productDTO = new ProductDTO();
                    productDTO.setProdNo(tuple.get(0, Integer.class)); // 상품 번호
                    productDTO.setProdName(tuple.get(1, String.class)); // 상품명
                    productDTO.setDescript(tuple.get(2, String.class)); // 상품설명
                    productDTO.setDiscount(tuple.get(3, Integer.class)); // 상품할인율
                    productDTO.setPrice(tuple.get(4, Integer.class)); // 상품가격
                    productDTO.setSeller(tuple.get(5, String.class)); // 상품판매자
                    productDTO.setDelivery(tuple.get(6, Integer.class)); // 배송
                    productDTO.setThumb1(tuple.get(7, String.class)); // 썸네일1
                    productDTO.setSold(tuple.get(8, Integer.class)); // 판매량
                    productDTO.setScore(tuple.get(9, Integer.class)); // 평점
                    productDTO.setReview(tuple.get(10, Integer.class)); // 후기
                    productDTO.setRdate(tuple.get(11, LocalDateTime.class)); // 등록날짜
                    productDTO.setCate1(tuple.get(12, Integer.class)); // cate1
                    productDTO.setCate2(tuple.get(13, Integer.class)); // cate2
                    productDTO.setCate3(tuple.get(14, Integer.class)); // cate3

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
                        String opNo = tuple.get(1,String.class);
                        Product product = tuple.get(2, Product.class);

                        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
                        productDTO.setCount(count);
                        productDTO.setOpNo(opNo);
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

    @Transactional
    public ResponseEntity<?> saveOrder (OrderDTO orderDTO){

        // product_order에 넣기
        Order order = modelMapper.map(orderDTO, Order.class);

        Order saveOrder = orderRepository.save(order);

        OrderDTO savedOrderDTO = modelMapper.map(saveOrder, OrderDTO.class);

        // 포인트 감소는~ orderItem에서
        int usePoint = orderDTO.getUsedPoint();

        return ResponseEntity.ok(savedOrderDTO);
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
