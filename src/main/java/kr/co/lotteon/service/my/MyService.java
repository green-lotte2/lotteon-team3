package kr.co.lotteon.service.my;

import com.querydsl.core.Tuple;
import kr.co.lotteon.dto.admin.AdminBoardPageResponseDTO;
import kr.co.lotteon.dto.cs.BoardDTO;
import kr.co.lotteon.dto.cs.CsPageRequestDTO;
import kr.co.lotteon.dto.cs.CsPageResponseDTO;
import kr.co.lotteon.dto.member.CouponDTO;

import kr.co.lotteon.dto.member.MemberDTO;
import kr.co.lotteon.dto.member.point.PointDTO;
import kr.co.lotteon.dto.member.point.PointPageRequestDTO;
import kr.co.lotteon.dto.member.point.PointPageResponseDTO;
import kr.co.lotteon.dto.product.*;
import kr.co.lotteon.entity.cs.BoardEntity;
import kr.co.lotteon.entity.member.Coupon;
import kr.co.lotteon.entity.member.Member;
import kr.co.lotteon.entity.member.Point;

import kr.co.lotteon.entity.product.Order;
import kr.co.lotteon.entity.product.OrderItem;
import kr.co.lotteon.entity.product.Review;
import kr.co.lotteon.repository.cs.BoardRepository;
import kr.co.lotteon.repository.member.MemberRepository;
import kr.co.lotteon.repository.my.CouponRepository;
import kr.co.lotteon.repository.my.PointRepository;
import kr.co.lotteon.repository.product.OrderItemRepository;
import kr.co.lotteon.repository.product.ProductRepository;
import kr.co.lotteon.repository.product.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyService {

    private final CouponRepository couponRepository;
    private final ModelMapper modelMapper;
    private final OrderItemRepository orderItemRepository;
    private final BoardRepository boardRepository;
    private final PointRepository pointRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;

    public List<CouponDTO> findCouponsByUid(String uid){
        log.info("내 쿠폰"+couponRepository.findCouponsByUid(uid));
        List<Coupon> result=couponRepository.findCouponsByUid(uid);
        List<CouponDTO> couponDTOS=result.stream().map(coupons->modelMapper.map(coupons,CouponDTO.class))
                .collect(Collectors.toList());
        for (CouponDTO couponDTO : couponDTOS) {
            couponDTO.changeUseYnString();
        }
        return couponDTOS;
    }

    public int findCouponCountByUidAndUseYn(String uid) {
        return couponRepository.countByUidAndUseYn(uid, "Y");
    }

    public int countOrderItemsByUidAndOrdStatusIn(String uid, List<String> ordStatusList) {
        return orderItemRepository.countByUidAndOrdStatusIn(uid, ordStatusList);
    }

    public int countByUidAndStatusIn(String uid,List<String> statusList) {
        return boardRepository.countByUidAndStatusIn(uid, statusList);
    }

    public List<BoardDTO> findByBoardAndUid(String uid) {

        log.info("내 문의들"+couponRepository.findCouponsByUid(uid));
        List<BoardEntity> result = boardRepository.findByUid(uid);

        return result.stream().map(boards->modelMapper.map(boards,BoardDTO.class))
                .collect(Collectors.toList());
    }

    public int countByUid(String uid){

        return boardRepository.countByUid(uid);
    }

    // point 리스트 출력(전체)
    public PointPageResponseDTO getPointListByUid(String uid, PointPageRequestDTO pointPageRequestDTO) {
        Pageable pageable = pointPageRequestDTO.getPageable();
        Page<Point> pointPage = pointRepository.findByUidOrderByCurrentDateDesc(uid, pageable);

        return new PointPageResponseDTO(
                pointPageRequestDTO,
                pointPage.getContent().stream()
                        .map(Point::toDTO)
                        .collect(Collectors.toList()),
                (int) pointPage.getTotalElements()
        );
    }

    // point 리스트 출력(선택)
    public PointPageResponseDTO findPointList(String uid, PointPageRequestDTO pointPageRequestDTO) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        LocalDateTime begin = LocalDateTime.parse(pointPageRequestDTO.getBegin() + "T00:00:00", formatter);

        LocalDateTime end = LocalDateTime.parse(pointPageRequestDTO.getEnd() + "T23:59:59", formatter);
        Page<Point> result = pointRepository.findByUidAndCurrentDateBetweenOrderByCurrentDateDesc(uid, begin, end, pointPageRequestDTO.getPageable());

        List<PointDTO> dtoList = result
                .getContent()
                .stream()
                .map(entity -> modelMapper.map(entity, PointDTO.class))
                .toList();
        int totalElement = (int) result.getTotalElements();
        return PointPageResponseDTO.builder()
                .pointPageRequestDTO(pointPageRequestDTO)
                .dtoList(dtoList)
                .total(totalElement)
                .build();
    }




    public CsPageResponseDTO QnaList(String uid,CsPageRequestDTO csPageRequestDTO){

        log.info("문의 목록 조회 1" + csPageRequestDTO);

        Pageable pageable = csPageRequestDTO.getPageable("bno");

        Page<Tuple> tuples = boardRepository.memberSelectBoards(uid,csPageRequestDTO, pageable);
        log.info("문의 목록 조회 2" + tuples);

        // Page<Product>를 List<ProductDTO>로 변환
        List<BoardDTO> boardDTOS = tuples.getContent().stream()
                .map(tuple -> {
                    BoardEntity board=tuple.get(0,BoardEntity.class);
                    String cateName=tuple.get(1,String.class);

                    BoardDTO boardDTO=modelMapper.map(board,BoardDTO.class);
                    boardDTO.setCateName(cateName);

                    return boardDTO;
                })
                .toList();
        log.info("문의 목록 조회 3" + boardDTOS);

        int total = (int) tuples.getTotalElements();

        return CsPageResponseDTO.builder()
                .csPageRequestDTO(csPageRequestDTO)
                .dtoList(boardDTOS)
                .total(total)
                .build();
    }

    public ProductReviewPageResponseDTO reviewList(String uid, ProductReviewPageRequestDTO productReviewPageRequestDTO){

        log.info("리뷰 목록 조회 1" + productReviewPageRequestDTO);

        Pageable pageable = productReviewPageRequestDTO.getPageable("rdate");

        Page<Tuple> tuples = productRepository.memberSelectReview(uid, productReviewPageRequestDTO, pageable);

        log.info("리뷰 목록 조회 2" + tuples.getContent());

        List<ReviewDTO> reviewDTOS = tuples.getContent().stream()
                .map(tuple -> {
                    Review review=tuple.get(0,Review.class);
                    String prodName=tuple.get(1,String.class);
                    int cate1=tuple.get(2,Integer.class);
                    int cate2=tuple.get(3,Integer.class);

                    ReviewDTO reviewDTO=modelMapper.map(review,ReviewDTO.class);
                    reviewDTO.setProdName(prodName);
                    reviewDTO.setCate1(cate1);
                    reviewDTO.setCate2(cate2);

                    return reviewDTO;
                })
                .toList();
        log.info("리뷰 목록 조회 3" + reviewDTOS);

        int total = (int) tuples.getTotalElements();

        return ProductReviewPageResponseDTO.builder()
                .productReviewPageRequestDTO(productReviewPageRequestDTO)
                .dtoList(reviewDTOS)
                .total(total)
                .build();


    }

    // 포인트 적립날짜 최신순 5개 출력
    public List<PointDTO> selectByUidAndDate(String uid){
        List<Point> points = pointRepository.selectPointByUidAndDate(uid);
        log.info("포인트 : "+points);
        return points.stream().map(point -> modelMapper.map(point,PointDTO.class))
                .collect(Collectors.toList());
    }
    
    // 문의내역 최신순 5개 출력
    public List<BoardDTO>selectReviewsByUidAndRdate(String uid){
        List<Tuple> boards = boardRepository.selectReviewsByUidAndRdate(uid);
        List<BoardDTO> boardDTOS=new ArrayList<>();
        boards.forEach(tuple -> {
            BoardEntity board=tuple.get(0,BoardEntity.class);
            String cateName=tuple.get(1,String.class);

            BoardDTO boardDTO=modelMapper.map(board,BoardDTO.class);
            boardDTO.setCateName(cateName);
            boardDTOS.add(boardDTO);
        });
        return boardDTOS;
    }
    
    // 리뷰내역 최신순 5개 출력
    public List<ReviewDTO>selectReviewByRdate(String uid){
        List<Tuple> reviews=productRepository.selectReviewByRdate(uid);
        List<ReviewDTO> reviewDTOS=new ArrayList<>();
        reviews.forEach(tuple -> {
            Review review=tuple.get(0,Review.class);
            String prodName=tuple.get(1,String.class);
            int cate1=tuple.get(2,Integer.class);
            int cate2=tuple.get(3,Integer.class);

            ReviewDTO reviewDTO=modelMapper.map(review,ReviewDTO.class);
            reviewDTO.setProdName(prodName);
            reviewDTO.setCate1(cate1);
            reviewDTO.setCate2(cate2);
            reviewDTOS.add(reviewDTO);

        });
        return reviewDTOS;
    }
    // 주문내역 최신순 5개 출력
    public  LinkedHashMap<Integer, List<OrderItemDTO>> selectOrder (String uid){

        log.info("주문내역 : "+orderItemRepository.selectOrder(uid).values());

        return orderItemRepository.selectOrder(uid);

    }


    // 최근 주문내역 최신순 5개 출력
    public List<OrderItemDTO>selectOrdersByUid(String uid){
        List<Tuple> orderItems = orderItemRepository.selectOrdersByUid(uid);
        List<OrderItemDTO> orderItemDTOS=new ArrayList<>();
        orderItems.forEach(tuple -> {
            OrderItem orderItem=tuple.get(0,OrderItem.class);
            String company=tuple.get(1,String.class);
            String prodName=tuple.get(2,String.class);
            int price=tuple.get(3,Integer.class); // 상품 개당 가격
            int discount=tuple.get(4,Integer.class);
            String thumb3=tuple.get(5,String.class);

            OrderItemDTO orderItemDTO=modelMapper.map(orderItem,OrderItemDTO.class);
            orderItemDTO.setCompany(company);
            orderItemDTO.setProdName(prodName);
            orderItemDTO.setPrice(price);
            orderItemDTO.setDiscount(discount);
            orderItemDTO.setThumb3(thumb3);

            // 상품 개별 총 가격(할인적용가) = (count * price) - discount
            int count=orderItemDTO.getCount();
            log.info("상품 개수 : "+count);
            int totalPricePerProduct=(count*price)-discount;
            log.info("상품 개별 가격(할인 적용 전) : "+count*price);
            log.info("상품 개별 총 가격(할인 적용 후) : "+totalPricePerProduct);

            orderItemDTO.setTotalPricePerProduct(totalPricePerProduct);

            orderItemDTOS.add(orderItemDTO);

        });

        return orderItemDTOS;
    }


    // 최근 주문내역 전체 출력
    public OrderItemPageResponseDTO selectWholeOrdersByUid(String uid, Pageable pageable, OrderItemPageRequestDTO orderItemPageRequestDTO){

        // 페이징된 결과와 함께 총 엔티티 수를 함께 반환(전체 페이지 수 계산 가능)
        Page<Tuple> results = orderItemRepository.selectWholeOrdersByUid(uid, pageable, orderItemPageRequestDTO);

        // map 함수를 사용하여 각 Tuple을 OrderItemDTO로 매핑하고, 그 결과를 리스트로 변환
        List<OrderItemDTO> dtoList = results.stream()
                .map(tuple -> {
                    OrderItem orderItem = tuple.get(0,OrderItem.class);
                    String company=tuple.get(1,String.class);
                    String prodName=tuple.get(2,String.class);
                    int price=tuple.get(3,Integer.class); // 상품 개당 가격
                    int discount=tuple.get(4,Integer.class);
                    String thumb3=tuple.get(5,String.class);
                    String ordStatus=tuple.get(6, String.class);

                    OrderItemDTO orderItemDTO = modelMapper.map(orderItem, OrderItemDTO.class);
                    orderItemDTO.setCompany(company);
                    orderItemDTO.setProdName(prodName);
                    orderItemDTO.setPrice(price);
                    orderItemDTO.setDiscount(discount);
                    orderItemDTO.setThumb3(thumb3);
                    orderItemDTO.setOrdStatus(ordStatus);

                    // 할인 가격으로 정의
                    int totalPrice = orderItemDTO.getPrice() - (orderItemDTO.getPrice() * orderItemDTO.getDiscount() / 100);
                    orderItemDTO.setTotalPricePerProduct(totalPrice);

                    return orderItemDTO;

                })
                .toList();

        // total 값
        int total = (int) results.getTotalElements();

        // List<ProductDTO>와 page 정보 리턴
        return OrderItemPageResponseDTO.builder()
                .orderItemPageRequestDTO(orderItemPageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    // 최근 주문내역 날짜 검색 출력
    public OrderItemPageResponseDTO selectOrdersByDate(String uid, Pageable pageable, OrderItemPageRequestDTO orderItemPageRequestDTO){

        Page<Tuple> results = orderItemRepository.selectOrdersByDate(uid, pageable , orderItemPageRequestDTO);

        List<OrderItemDTO> dtoList = results.stream()
                .map(tuple -> {
                    OrderItem orderItem = tuple.get(0,OrderItem.class);
                    String company=tuple.get(1,String.class);
                    String prodName=tuple.get(2,String.class);
                    int price=tuple.get(3,Integer.class); // 상품 개당 가격
                    int discount=tuple.get(4,Integer.class);
                    String thumb3=tuple.get(5,String.class);
                    String ordStatus=tuple.get(6, String.class);

                    OrderItemDTO orderItemDTO = modelMapper.map(orderItem, OrderItemDTO.class);
                    orderItemDTO.setCompany(company);
                    orderItemDTO.setProdName(prodName);
                    orderItemDTO.setPrice(price);
                    orderItemDTO.setDiscount(discount);
                    orderItemDTO.setThumb3(thumb3);
                    orderItemDTO.setOrdStatus(ordStatus);

                    int totalPrice = orderItemDTO.getPrice() - (orderItemDTO.getPrice() * orderItemDTO.getDiscount() / 100);
                    orderItemDTO.setTotalPricePerProduct(totalPrice);

                    return orderItemDTO;

                })
                .toList();

        // total 값
        int total = (int) results.getTotalElements();

        // List<ProductDTO>와 page 정보 리턴
        return OrderItemPageResponseDTO.builder()
                .orderItemPageRequestDTO(orderItemPageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    // 판매자 정보 출력
    public MemberDTO selectSellerByCompany(String company){
        Member seller = memberRepository.selectSellerByCompany(company);
        MemberDTO sellerInfo = modelMapper.map(seller, MemberDTO.class);

        log.info("판매자 정보 DTO : "+sellerInfo);
        return sellerInfo;

    }


    public void writeReview(ReviewDTO reviewDTO){
        Review review = modelMapper.map(reviewDTO, Review.class);
        reviewRepository.save(review);
    }







}
