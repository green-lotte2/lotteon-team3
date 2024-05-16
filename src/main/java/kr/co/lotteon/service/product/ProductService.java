package kr.co.lotteon.service.product;

import com.querydsl.core.Tuple;
import jakarta.transaction.Transactional;
import kr.co.lotteon.dto.cs.BoardDTO;
import kr.co.lotteon.dto.cs.BoardTypeDTO;
import kr.co.lotteon.dto.cs.CommentDTO;
import kr.co.lotteon.dto.member.CouponDTO;
import kr.co.lotteon.dto.product.*;
import kr.co.lotteon.entity.cs.BoardEntity;
import kr.co.lotteon.entity.cs.BoardTypeEntity;
import kr.co.lotteon.entity.cs.Comment;
import kr.co.lotteon.entity.member.Coupon;
import kr.co.lotteon.entity.product.*;
import kr.co.lotteon.mapper.CouponMapper;
import kr.co.lotteon.mapper.MemberMapper;
import kr.co.lotteon.mapper.ProductMapper;
import kr.co.lotteon.repository.cs.BoardRepository;
import kr.co.lotteon.repository.my.CouponRepository;
import kr.co.lotteon.repository.product.OptionRepository;
import kr.co.lotteon.repository.product.OrderItemRepository;
import kr.co.lotteon.repository.product.OrderRepository;
import kr.co.lotteon.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSession;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.Console;
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
    private final OrderItemRepository orderItemRepository;
    private final BoardRepository boardRepository;
    private final CouponRepository couponRepository;
    private final MemberMapper memberMapper;
    private final CouponMapper couponMapper;
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


    // 상품 문의 조회 - 상품 보기 페이지
    public ProductQnaPageResponseDTO selectQna(int prodNo){
        ProductReviewPageRequestDTO pageRequestDTO = new ProductReviewPageRequestDTO();

        Pageable pageable = pageRequestDTO.getPageable("no");
        Page<Tuple> result = boardRepository.selectQna(prodNo, pageable);

        List<ProductQnaDTO> dtoList = result.stream()
                .map(tuple -> {
                    BoardEntity boardEntity = tuple.get(0, BoardEntity.class);
                    BoardTypeEntity typeEntity = tuple.get(1, BoardTypeEntity.class);
                    Comment commentEntity = tuple.get(2, Comment.class);

                    BoardDTO board = modelMapper.map(boardEntity, BoardDTO.class);
                    BoardTypeDTO type = modelMapper.map(typeEntity, BoardTypeDTO.class);


                    ProductQnaDTO productQnaDTO = new ProductQnaDTO();
                    productQnaDTO.setBoardDTO(board);
                    productQnaDTO.setBoardTypeDTO(type);

                    if(commentEntity != null) {
                        CommentDTO comment = modelMapper.map(commentEntity, CommentDTO.class);
                        productQnaDTO.setCommentDTO(comment);
                    }
                    return productQnaDTO;

                })
                .toList();
        // total 값
        int total = (int) result.getTotalElements();

        return ProductQnaPageResponseDTO.builder()
                .productReviewPageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
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

                        String opNos = productDTO.getOpNo();
                        log.info("오더 조회 서비스 옵션번호 : " + opNos);
                        if(opNos != null){
                            // 옵션 뽑아내기
                            String[] optionString = opNos.split(",");
                            int[] optionIds = new int[optionString.length];

                            for (int i = 0; i < optionString.length; i++) {
                                optionIds[i] = Integer.parseInt(optionString[i].trim());
                            }
                            log.info("오더 조회 서비스 옵션번호2 : " + Arrays.toString(optionIds));

                            List<OptionDTO> options = new ArrayList<>();
                            for(int optionNos : optionIds){
                                OptionDTO option = optionRepository.selectOptionForCart(optionNos);
                                log.info("오더 조회 서비스 옵션번호3 :" + option);

                                if (option != null){
                                    options.add(option);
                                }
                                productDTO.setOptionList(options);
                            }
                        }


                        return productDTO;
                    })
                    .collect(Collectors.toList());
            log.info("오더 조회 서비스 3"+mappedProductDTOs);

            // 선언해둔 productDTOS에 모두 덮어씌우기
            productDTOS.addAll(mappedProductDTOs);
        }
        return productDTOS;
    }

    // 상품보기에서 바로 주문
    public ProductDTO prodToOrder(int prodNo){
        Product result = productRepository.findById(prodNo).get();

        return modelMapper.map(result, ProductDTO.class);
    }

    // product_order 테이블에 저장, memberPoint에 적립
    @Transactional
    public ResponseEntity<?> saveOrder (OrderDTO orderDTO){

        // 적립될 포인트
        int savePoint = orderDTO.getSavePoint();
        // 사용한 포인트
        int usedPoint = orderDTO.getUsedPoint();
        // 구매한 회원 아이디
        String uid = orderDTO.getOrdUid();

        if(orderDTO.getCouponSeq() != null) {
            // 사용한 쿠폰 번호
            int couponSeq = orderDTO.getCouponSeq();
            // 쿠폰 사용하기
            couponMapper.updateUseYn(couponSeq);
        }
        // product_order에 넣기
        Order order = modelMapper.map(orderDTO, Order.class);
        Order saveOrder = orderRepository.save(order);
        log.info("오더서비스 : " + saveOrder.getOrdNo());
        log.info("오더서비스 사용 포인트 : " + saveOrder.getUsedPoint());
        OrderDTO savedOrderDTO = modelMapper.map(saveOrder, OrderDTO.class);

        // 사용한 포인트만큼 포인트 감소
        if (saveOrder.getUsedPoint()>0){
            memberMapper.updateMemberPoint(uid, usedPoint);
        }

        return ResponseEntity.ok(savedOrderDTO);
    }

    @Transactional
    public int saveOrderItem (List<OrderItemDTO> orderItemDTOS, String uid, int ordNo){

        for (OrderItemDTO orderItemDTO : orderItemDTOS) {
            // 주문 상품 저장
            OrderItem orderItem = modelMapper.map(orderItemDTO, OrderItem.class);
            orderItem.setUid(uid);
            orderItem.setOrdNo(ordNo);
            orderItem.setOrdStatus("배송준비");
            orderItemRepository.save(orderItem);

            // 상품 수량 감소
            productMapper.updateProductStock(orderItemDTO.getProdNo(), orderItemDTO.getCount());
        }
        return ordNo;
    }

    // 주문 완료 페이지
    public List<OrderItemDTO> selectOrderComplete(int orderNo){
        log.info("주문완료 서비스..1 "+orderNo);
        List<Tuple> orderItemTuple= orderItemRepository.selectOrderComplete(orderNo);

        log.info("주문완료 서비스..2"+orderItemTuple);
        List<OrderItemDTO> result = orderItemTuple.stream()
                .map(tuple -> {

                    OrderItem orderItem = tuple.get(9, OrderItem.class);

                    OrderItemDTO orderItemDTO = modelMapper.map(orderItem, OrderItemDTO.class);
                    orderItemDTO.setProdName(tuple.get(0, String.class));
                    orderItemDTO.setDescript(tuple.get(1, String.class));
                    orderItemDTO.setCompany(tuple.get(2, String.class));
                    orderItemDTO.setPrice(tuple.get(3, Integer.class));
                    orderItemDTO.setDiscount(tuple.get(4, Integer.class));
                    orderItemDTO.setThumb3(tuple.get(5, String.class));
                    orderItemDTO.setCate1(tuple.get(6, Integer.class));
                    orderItemDTO.setCate2(tuple.get(7, Integer.class));
                    orderItemDTO.setCate3(tuple.get(8, Integer.class));


                    // 옵션 뽑아내기
                    if (!orderItemDTO.getOpNo().equals( "")){
                        String[] optionString = orderItemDTO.getOpNo().split(",");
                        int[] optionIds = new int[optionString.length];

                        for (int i = 0; i < optionString.length; i++) {
                            optionIds[i] = Integer.parseInt(optionString[i].trim());
                        }

                        List<OptionDTO> options = new ArrayList<>();
                        for(int optionNos : optionIds){
                            OptionDTO option = optionRepository.selectOptionForCart(optionNos);

                            if (option != null){
                                options.add(option);
                            }
                            orderItemDTO.setOptionList(options);
                        }
                    }


                    return orderItemDTO;
                })
                .collect(Collectors.toList());

        log.info("주문완료 서비스..3"+result);

        return result;
    }

    // 주문완료 주문정보 가져오기
    public OrderDTO selectOrder (int ordNo){
         Optional<Order> order = orderRepository.findById(ordNo);

        return modelMapper.map(order, OrderDTO.class);
    }
    
    // 쿠폰 정보 가져오기
    public List<CouponDTO> selectsCouponsNotUse(String uid){

        List<Coupon> coupons = couponRepository.findCouponsByUid(uid);
        List<CouponDTO> result = null;
        if (!coupons.isEmpty()){
           result =  couponRepository
                                    .selectsCouponsNotUse(uid)
                                    .stream()
                                    .map(dto->modelMapper.map(dto, CouponDTO.class))
                                    .collect(Collectors.toList());
        }
        log.info("쿠폰서비스 : " + result);
        return result;
    }
    // ========== 메인페이지 ==========
    // 최신상품
    public List<ProductDTO> bestProductMain(){return productRepository.bestProductMain();}
    public List<ProductDTO> bestProductMain2(){return productRepository.bestProductMain2();}
    public List<ProductDTO> recentProductMain(){return productRepository.recentProductMain();}
    public List<ProductDTO> discountProductMain(){return productRepository.discountProductMain();}
    public List<ProductDTO> hitProductMain(){return productRepository.hitProductMain();}
    public List<ProductDTO> recommendProductMain(){return productRepository.recommendProductMain();}
    // ==============================


}
