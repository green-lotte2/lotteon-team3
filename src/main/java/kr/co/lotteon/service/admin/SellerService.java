package kr.co.lotteon.service.admin;

import com.querydsl.core.Tuple;
import kr.co.lotteon.dto.admin.*;
import kr.co.lotteon.dto.cs.BoardCateDTO;
import kr.co.lotteon.dto.cs.BoardDTO;
import kr.co.lotteon.dto.cs.BoardFileDTO;
import kr.co.lotteon.dto.cs.BoardTypeDTO;
import kr.co.lotteon.dto.product.*;
import kr.co.lotteon.entity.cs.BoardCateEntity;
import kr.co.lotteon.entity.cs.BoardEntity;
import kr.co.lotteon.entity.cs.BoardTypeEntity;
import kr.co.lotteon.entity.product.*;
import kr.co.lotteon.mapper.OrderItemMapper;
import kr.co.lotteon.mapper.ProductMapper;
import kr.co.lotteon.repository.admin.BannerRepository;
import kr.co.lotteon.repository.cs.BoardCateRepository;
import kr.co.lotteon.repository.cs.BoardFileRepository;
import kr.co.lotteon.repository.cs.BoardRepository;
import kr.co.lotteon.repository.cs.BoardTypeRepository;
import kr.co.lotteon.repository.member.MemberRepository;
import kr.co.lotteon.repository.member.TermsRepository;
import kr.co.lotteon.repository.product.*;
import kr.co.lotteon.security.MyUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SellerService {

    private final BoardRepository boardRepository;
    private final BoardCateRepository boardCateRepository;
    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;
    private final Cate1Repository cate1Repository;
    private final Cate2Repository cate2Repository;
    private final Cate3Repository cate3Repository;
    private final TermsRepository termsRepository;
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final BoardTypeRepository typeRepository;
    private final BannerRepository bannerRepository;
    private final BoardFileRepository fileRepository;
    private final OrderItemRepository orderItemRepository;

    private final ModelMapper modelMapper;
    private final ProductMapper productMapper;
    private final OrderItemMapper orderItemMapper;

    @Value("${img.upload.path}")
    private String imgUploadPath;

    // 판매자 인덱스 주문 차트 조회
    public List<Map<String, Object>> selectOrderForChart() {
        log.info("월별 주문 count 조회 Serv 1");
        List<Tuple> tuples = orderRepository.selectOrderForChart();
        log.info("월별 주문 count 조회 Serv 2: " + tuples);

        // 총 주문 수를 저장할 변수
        AtomicLong totalOrders = new AtomicLong(0);

        List<Map<String, Object>> jsonResult = tuples.stream()
                .map(tuple -> {
                    int year = tuple.get(0, Integer.class);
                    int month = tuple.get(1, Integer.class);
                    long count = tuple.get(2, long.class);

                    // 총 주문 수에 현재 월의 주문 수를 더함
                    totalOrders.addAndGet(count);

                    Map<String, Object> map = new HashMap<>();
                    map.put("month", month + "월");
                    map.put("count", count);
                    return map;
                })
                .collect(Collectors.toList());

        // 총 주문 수를 결과에 추가
        Map<String, Object> totalMap = new HashMap<>();
        totalMap.put("total", totalOrders.get());
        jsonResult.add(totalMap);

        log.info("월별 주문 count 조회 Serv 3: " + jsonResult);
        return jsonResult;
    }

    // 판매자 인덱스 공지사항 조회
    public List<BoardDTO> adminSelectNotices() {
        List<Tuple> results = boardRepository.adminSelectBoards("notice");
        List<BoardDTO> dtoList = new ArrayList<>();
        results.forEach(tuple -> {
            // Tuple -> Entity
            BoardEntity board = tuple.get(0, BoardEntity.class);
            String typeName = tuple.get(1, String.class);
            // Entity -> DTO
            BoardDTO boardDTO = modelMapper.map(board, BoardDTO.class);
            boardDTO.setTypeName(typeName);
            dtoList.add(boardDTO);
        });

        return dtoList;
    }

    // 판매자 인덱스 고객문의 조회
    public List<BoardDTO> adminSelectQnas() {
        List<Tuple> results = boardRepository.adminSelectBoards("qna");
        List<BoardDTO> dtoList = new ArrayList<>();
        results.forEach(tuple -> {
            // Tuple -> Entity
            BoardEntity board = tuple.get(0, BoardEntity.class);
            String typeName = tuple.get(1, String.class);
            // Entity -> DTO
            BoardDTO boardDTO = modelMapper.map(board, BoardDTO.class);
            boardDTO.setTypeName(typeName);
            dtoList.add(boardDTO);
        });

        return dtoList;
    }

    // 판매자 인덱스 주문 내역 카드 조회
    public OrderCardDTO selectCountSumByPeriod(LocalDateTime period){

        // 현재 로그인 중인 사용자 정보 불러오기
        String sellerId = whoAmI();
        log.info("sellerId : "+sellerId);

        // 해당 판매자의 상품번호 전부 조회
        List<Integer> prodNos = productRepository.selectProdNoForQna(sellerId);
        log.info("상품번호 전부 조회 "+ prodNos.toString());

        Tuple result = orderItemRepository.selectCountSumByPeriod(period, prodNos);
        OrderCardDTO orderCardDTO = new OrderCardDTO();
        orderCardDTO.setOrderCount(result.get(0, Long.class));
        orderCardDTO.setOrderSum(result.get(1, Integer.class));

        return orderCardDTO;

    }
    // 판매자 인덱스 배송현황 카드 조회
    public Map<String, Long> findOrdStatus(){
        String sellerId = whoAmI();

        // 해당 판매자의 상품번호 전부 조회
        List<Integer> prodNos = productRepository.selectProdNoForQna(sellerId);

        List<Tuple> results = orderItemRepository.selectOrdStatusCount(prodNos);
        Map<String, Long> ordStatus = results.stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(0, String.class), // 주문 상태
                        tuple -> tuple.get(1, Long.class)   // 주문 수
                ));
        return ordStatus;
    }

    // 판매자 주문 현황 차트 조회
    public List<Map<String, Object>> findOrderForChart(){

        String sellerId = whoAmI();

        List<Integer> prodNos = productRepository.selectProdNoForQna(sellerId);

         List<Tuple> tuples = orderItemRepository.selectSumByPeriod(prodNos);

        // 총 주문 수를 저장할 변수
        AtomicLong totalOrders = new AtomicLong(0);

        List<Map<String, Object>> jsonResult = tuples.stream()
                .map(tuple -> {
                    String period = tuple.get(0, String.class);
                    long count = tuple.get(1, long.class);

                    // 총 주문 수에 현재 월의 주문 수를 더함
                    totalOrders.addAndGet(count);

                    Map<String, Object> map = new HashMap<>();
                    map.put("month", period + "월");
                    map.put("count", count);
                    return map;
                })
                .collect(Collectors.toList());

        // 총 주문 수를 결과에 추가
        Map<String, Object> totalMap = new HashMap<>();
        totalMap.put("total", totalOrders.get());
        jsonResult.add(totalMap);

        log.info("월별 주문 count 조회 Serv 3: " + jsonResult);
        return jsonResult;
    }

    // 판매자 게시판 관리 - 기본 게시글 목록 출력
    public AdminBoardPageResponseDTO findBoardByGroup(String cate, AdminBoardPageRequestDTO adminBoardPageRequestDTO) {
        String group = adminBoardPageRequestDTO.getGroup();
        String keyword = adminBoardPageRequestDTO.getKeyword();
        Pageable pageable = adminBoardPageRequestDTO.getPageable("bno");
        log.info("게시판관리 - 목록 Serv 1 : " + adminBoardPageRequestDTO);
        log.info("게시판관리 - 목록 Serv 2 cate : " + cate);

        Page<Tuple> boardEntities = null;

        // 현재 로그인 중인 사용자 정보 불러오기
        String sellerId = whoAmI();

        // 해당 판매자의 상품번호 전부 조회
        List<Integer> prodNos = productRepository.selectProdNoForQna(sellerId);
        log.info("상품번호 전부 조회 "+ prodNos.toString());


        // 전체 조회
        if ((keyword == null || "".equals(keyword)) && ("".equals(cate) || "all".equals(cate))) {
            // DB 조회
            boardEntities = boardRepository.selectBoardBySeller(adminBoardPageRequestDTO, pageable, prodNos);
            log.info("게시판관리 - 목록 Serv 3 전체 조회 : " + boardEntities);

            // type이 cate인 검색
        } else if ((keyword == null || "".equals(keyword)) && !"all".equals(cate)) {
            // DB 조회
            boardEntities = boardRepository.searchBoardBySellerAndCate(adminBoardPageRequestDTO, pageable, prodNos, cate);
            log.info("게시판관리 - 목록 Serv 4 cate인 검색 : " + boardEntities);

            // 키워드로 검색
        } else if (keyword != null) {
            // DB 조회
            boardEntities = boardRepository.searchBoardsBySellerAndKeyword(adminBoardPageRequestDTO, pageable, prodNos);
            log.info("게시판관리 - 목록 Serv 5 키워드로 검색 : " + boardEntities);
        }


        // Page<Tuple>을 List<BoardDTO>로 변환
        List<BoardDTO> dtoList = boardEntities.getContent().stream()
                .map(tuple -> {
                    log.info("tuple : " + tuple);
                    // Tuple -> Board 테이블 칼럼
                    BoardEntity boardEntity = tuple.get(0, BoardEntity.class);
                    // Tuple -> Join한 typeName 칼럼
                    String typeName = tuple.get(1, String.class);
                    // Tuple -> Join한 cateName 칼럼
                    String cateName = tuple.get(2, String.class);
                    // Tuple -> Join한 cateName 칼럼
                    String nick = tuple.get(3, String.class);
                    // Entity -> DTO
                    BoardDTO boardDTO = modelMapper.map(boardEntity, BoardDTO.class);
                    boardDTO.setTypeName(typeName);
                    boardDTO.setCateName(cateName);
                    boardDTO.setNick(nick);

                    log.info("게시판관리 - 목록 Serv 3 : " + boardDTO);

                    return boardDTO;
                })
                .toList();
        log.info("게시판관리 - 목록 Serv 4 : " + dtoList);

        // total 값
        int total = (int) boardEntities.getTotalElements();

        // List<ProductDTO>와 page 정보 리턴
        return AdminBoardPageResponseDTO.builder()
                .adminBoardPageRequestDTO(adminBoardPageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }
    // 판매자 주문 관리
    public List<Map<String, Object>> selectOrderForSeller() {
        log.info("월별 주문 count 조회 Serv 1");
        // 현재 로그인 중인 사용자 정보 불러오기
        String sellerId = whoAmI();

        // 해당 판매자의 상품번호 전부 조회
        List<Integer> prodNos = productRepository.selectProdNoForQna(sellerId);
        List<Tuple> tuples = orderItemRepository.selectOrderForSeller(prodNos);

        log.info("월별 주문 count 조회 Serv 2: " + tuples);

        // 총 주문 수를 저장할 변수
        AtomicLong totalOrders = new AtomicLong(0);

        List<Map<String, Object>> jsonResult = tuples.stream()
                .map(tuple -> {
                    Integer year = tuple.get(0, Integer.class);
                    Integer month = tuple.get(1, Integer.class);
                    long count = tuple.get(2, Long.class);

                    // 총 주문 수에 현재 월의 주문 수를 더함
                    totalOrders.addAndGet(count);

                    Map<String, Object> map = new HashMap<>();
                    map.put("month", year + "년" + month + "월");
                    map.put("count", count);
                    return map;
                })
                .collect(Collectors.toList());
        // 총 주문 수를 결과에 추가
        Map<String, Object> totalMap = new HashMap<>();
        totalMap.put("total", totalOrders.get());
        jsonResult.add(totalMap);

        log.info("월별 주문 count 조회 Serv 3: " + jsonResult);
        return jsonResult;
    }
    // 판매자 주문 현황
    @Transactional
    public SellerOrderPageResponseDTO selectOrderList(AdminPageRequestDTO adminPageRequestDTO){
        log.info("판매자 주문 현황 Serv 1  ");
        Pageable pageable = adminPageRequestDTO.getPageable("no");
        // 현재 로그인 중인 사용자 정보 불러오기
        String sellerId = whoAmI();

        // 해당 판매자의 상품번호 전부 조회
        List<Integer> prodNos = productRepository.selectProdNoForQna(sellerId);
        log.info("판매자 주문 현황 Serv 2 : "+prodNos);


        // order, orderItem, product 정보 DB 조회
        Page<Tuple> results = orderItemRepository.selectOrderList(adminPageRequestDTO, pageable, prodNos);
        log.info("판매자 주문 현황 Serv 3 : " + results.getContent().size());


        List<OrderListDTO> dtoList = results.getContent().stream()
                .map(tuple -> {

                    OrderListDTO orderListDTO = new OrderListDTO();

                    // Tuple -> Entity
                    OrderItem orderItem = tuple.get(0, OrderItem.class);
                    Order order         = tuple.get(1, Order.class);
                    Product product     = tuple.get(2, Product.class);

                    // Entity -> DTO
                    OrderItemDTO orderItemDTO   = modelMapper.map(orderItem, OrderItemDTO.class);
                    OrderDTO orderDTO           = modelMapper.map(order, OrderDTO.class);
                    ProductDTO productDTO       = modelMapper.map(product, ProductDTO.class);

                    // opNos
                    String strOpNos = orderItemDTO.getOpNo();
                    log.info("strOpNos : " + strOpNos);

                    if(strOpNos != null && !strOpNos.equals("")) {

                        // String -> List<Integer>
                        List<Integer> opNos = Arrays.stream(strOpNos.split(","))
                                .map(Integer::parseInt)
                                .collect(Collectors.toList());

                        // optionList 조회
                        List<OptionDTO> options = optionRepository.selectOptionByOpNos(opNos)
                                .stream()
                                .map(entity -> modelMapper.map(entity, OptionDTO.class))
                                .toList();
                        log.info("options : "+ options);

                        orderListDTO.setOpList(options);
                    }

                    // DTO들을 OrderListDTO에 포함
                    orderListDTO.setOrderItemDTO(orderItemDTO);
                    orderListDTO.setOrderDTO(orderDTO);
                    orderListDTO.setProductDTO(productDTO);
                    log.info("stream 내부 orderListDTO : " + orderListDTO);
                    return orderListDTO;

                })
                .toList();

        log.info("판매자 주문 현황 Serv 4 : " + dtoList);

        // total 값
        int total = (int) results.getTotalElements();

        // List<OrderListDTO>와 page 정보 리턴
        return SellerOrderPageResponseDTO.builder()
                .adminPageRequestDTO(adminPageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }
    // 판매자 주문 검색 현황
    @Transactional
    public SellerOrderPageResponseDTO searchOrderList(AdminPageRequestDTO adminPageRequestDTO){
        log.info("판매자 주문 검색 검색 Serv 1  ");
        Pageable pageable = adminPageRequestDTO.getPageable("no");

        if(adminPageRequestDTO.getType().equals("ordStatus")) {
            switch (adminPageRequestDTO.getKeyword()) {
                case "prepare":
                    adminPageRequestDTO.setKeyword("배송준비");
                    break;
                case "going":
                    adminPageRequestDTO.setKeyword("배송중");
                    break;
                case "delivered":
                    adminPageRequestDTO.setKeyword("배송완료");
                    break;
                case "cancel":
                    adminPageRequestDTO.setKeyword("주문취소");
                    break;
                case "exchange":
                    adminPageRequestDTO.setKeyword("교환요청");
                    break;
                case "refund":
                    adminPageRequestDTO.setKeyword("환불요청");
                    break;
                case "complete":
                    adminPageRequestDTO.setKeyword("처리완료");
                    break;
                case "done":
                    adminPageRequestDTO.setKeyword("구매확정");
                    break;
            }
        }
        // 현재 로그인 중인 사용자 정보 불러오기
        String sellerId = whoAmI();

        // 해당 판매자의 상품번호 전부 조회
        List<Integer> prodNos = productRepository.selectProdNoForQna(sellerId);

        // order, orderItem, product, option 정보 DB 조회
        Page<Tuple> results = orderItemRepository.searchOrderList(adminPageRequestDTO, pageable, prodNos);
        log.info("판매자 주문 검색 Serv 3 : " + results.getContent().size());
        List<OrderListDTO> dtoList = results.getContent().stream()
                .map(tuple -> {

                    OrderListDTO orderListDTO = new OrderListDTO();

                    // Tuple -> Entity
                    OrderItem orderItem = tuple.get(0, OrderItem.class);
                    Order order         = tuple.get(1, Order.class);
                    Product product     = tuple.get(2, Product.class);

                    // Entity -> DTO
                    OrderItemDTO orderItemDTO   = modelMapper.map(orderItem, OrderItemDTO.class);
                    OrderDTO orderDTO           = modelMapper.map(order, OrderDTO.class);
                    ProductDTO productDTO       = modelMapper.map(product, ProductDTO.class);

                    // opNos
                    String strOpNos = orderItemDTO.getOpNo();
                    log.info("strOpNos : " + strOpNos);

                    if(strOpNos != null && !strOpNos.equals("")) {

                        // String -> List<Integer>
                        List<Integer> opNos = Arrays.stream(strOpNos.split(","))
                                .map(Integer::parseInt)
                                .collect(Collectors.toList());

                        // optionList 조회
                        List<OptionDTO> options = optionRepository.selectOptionByOpNos(opNos)
                                .stream()
                                .map(entity -> modelMapper.map(entity, OptionDTO.class))
                                .toList();
                        log.info("options : "+ options);

                        orderListDTO.setOpList(options);
                    }

                    // DTO들을 OrderListDTO에 포함
                    orderListDTO.setOrderItemDTO(orderItemDTO);
                    orderListDTO.setOrderDTO(orderDTO);
                    orderListDTO.setProductDTO(productDTO);
                    return orderListDTO;

                })
                .toList();

        log.info("판매자 주문 검색 Serv 4 : " + dtoList);

        // total 값
        int total = (int) results.getTotalElements();

        // List<OrderListDTO>와 page 정보 리턴
        return SellerOrderPageResponseDTO.builder()
                .adminPageRequestDTO(adminPageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }
    // 판매자 주문 현황 옵션 조회
    // 판매자 주문 상태 변경
    public ResponseEntity<?> modifyOrdStatus(int ordItemno, String ordStatus){
        log.info("주문 상태 변경 Serv 1: " + ordItemno);
        log.info("주문 상태 변경 Serv 2: " + ordStatus);

        switch (ordStatus){
            case "prepare" :
                ordStatus = "배송준비";
                break;
            case "going" :
                ordStatus = "배송중";
                break;
            case "delivered" :
                ordStatus = "배송완료";
                break;
            case "cancel" :
                ordStatus = "주문취소";
                break;
            case "exchange" :
                ordStatus = "교환요청";
                break;
            case "refund" :
                ordStatus = "환불요청";
                break;
            case "complete" :
                ordStatus = "처리완료";
                break;
        }

        // 상태 업데이트
        orderItemMapper.updateOrdStatus(ordStatus, ordItemno);

        return ResponseEntity.ok().body("update stauts ...");
    }
    // 판매자 게시판 관리 - 게시글 검색 카테고리 조회
    public List<BoardCateDTO> findBoardCate() {

        List<BoardCateEntity> boardCates = boardCateRepository.findAll();
        // 조회된 Entity List -> DTO List
        return boardCates.stream()
                .map(cate -> modelMapper.map(cate, BoardCateDTO.class))
                .collect(Collectors.toList());
    }

    // 판매자 게시판 관리 - 게시글 검색 type(말머리) 조회
    public ResponseEntity<?> findBoardType(String cate) {

        List<BoardTypeEntity> boardTypes = typeRepository.findByCate(cate);
        // 조회된 Entity List -> DTO List
        List<BoardTypeDTO> typeList = boardTypes.stream()
                .map(type -> modelMapper.map(type, BoardTypeDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(typeList);
    }
    // 판매자 게시판 보기
    public BoardDTO selectBoard(int bno) {
        log.info("판매자 게시글 보기 Serv 1 : " + bno);
        // DB 조회
        Optional<BoardEntity> optEntity = boardRepository.findById(bno);
        // Entity
        BoardEntity boardEntity = optEntity.get();
        log.info("판매자 게시글 보기 Serv 2 : " + boardEntity);
        // Entity -> DTO
        BoardDTO boardDTO = modelMapper.map(boardEntity, BoardDTO.class);
        log.info("판매자 게시글 보기 Serv 3 : " + boardDTO);

        // file 조회
        List<BoardFileDTO> boardFileDTOS = fileRepository.findByBno(bno)
                .stream()
                .map(entity -> modelMapper.map(entity, BoardFileDTO.class))
                .toList();

        boardDTO.setFileDTOList(boardFileDTOS);
        return boardDTO;
    }

    // 이미지 리사이징 함수 - width, height
    public String imgResizing(MultipartFile file, String orgPath, String path, int targetWidth, int targetHeight) {
        String oName = file.getOriginalFilename();
        String ext = oName.substring(oName.lastIndexOf("."));

        String sName = UUID.randomUUID().toString() + ext;

        try {
            // 원본 파일 저장
            file.transferTo(new File(orgPath, sName));

            // 리사이징
            Thumbnails.of(new File(orgPath, sName))
                    .size(targetWidth, targetHeight)
                    .toFile(new File(path, sName));

            log.info("이미지 리사이징 완료: " + sName);

            return sName;
        } catch (IOException e) {
            log.error("이미지 리사이징 실패: " + e.getMessage());
            return null;
        }
    }

    // 이미지 리사이징 함수 - width만
    public String imgResizing(MultipartFile file, String orgPath, String path, int targetWidth) {
        String oName = file.getOriginalFilename();
        String ext = oName.substring(oName.lastIndexOf("."));

        String sName = UUID.randomUUID().toString() + ext;

        try {
            // 원본 파일 저장
            file.transferTo(new File(orgPath, sName));

            // 리사이징
            Thumbnails.of(new File(orgPath, sName))
                    .width(targetWidth)
                    .toFile(new File(path, sName));

            log.info("이미지 리사이징 완료: " + sName);

            return sName;
        } catch (IOException e) {
            log.error("이미지 리사이징 실패: " + e.getMessage());
            return null;
        }
    }

    // 판매자 매출 현황 차트 조회
    public List<Map<String, Object>> selectSalesChart(){
        String sellerId = whoAmI();

        // 총 매출액을 저장할 변수
        AtomicLong totalOrders = new AtomicLong(0);

        List<Map<String, Object>> jsonResult = orderItemRepository.selectSales(sellerId).stream()
                .map(tuple -> {
                    int year = tuple.get(0, Integer.class);
                    int month = tuple.get(1, Integer.class);
                    Integer sum = tuple.get(2, Integer.class);

                    // 총 매출액에 현재 월의 매출액를 더함
                    totalOrders.addAndGet(sum);

                    Map<String, Object> map = new HashMap<>();
                    map.put("month", year+ "년 "+ month + "월");
                    map.put("sum", sum);
                    return map;
                })
                .collect(Collectors.toList());

        // 총 매출액을 결과에 추가
        Map<String, Object> totalMap = new HashMap<>();
        totalMap.put("total", totalOrders.get());
        jsonResult.add(totalMap);

        return jsonResult;
    }

    // 사용자 정보 함수
    public String whoAmI(){
        // 현재 로그인 중인 사용자 정보 불러오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 로그인 중일 때 해당 사용자 id를 seller에 입력
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        String sellerId = userDetails.getMember().getNick();

        return sellerId;
    }

}
