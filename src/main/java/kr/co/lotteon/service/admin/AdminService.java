package kr.co.lotteon.service.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.Tuple;
import kr.co.lotteon.dto.admin.*;
import kr.co.lotteon.dto.company.RecruitDTO;
import kr.co.lotteon.dto.company.RecruitPageResponseDTO;
import kr.co.lotteon.dto.cs.*;
import kr.co.lotteon.dto.member.MemberDTO;
import kr.co.lotteon.dto.product.*;
import kr.co.lotteon.entity.admin.Article;
import kr.co.lotteon.entity.admin.Banner;
import kr.co.lotteon.entity.admin.Recruit;
import kr.co.lotteon.entity.cs.BoardCateEntity;
import kr.co.lotteon.entity.cs.BoardEntity;
import kr.co.lotteon.entity.cs.BoardTypeEntity;
import kr.co.lotteon.entity.member.Member;
import kr.co.lotteon.entity.member.Terms;
import kr.co.lotteon.entity.product.*;
import kr.co.lotteon.mapper.ProductMapper;
import kr.co.lotteon.repository.ArticleRepository;
import kr.co.lotteon.repository.BannerRepository;
import kr.co.lotteon.repository.RecruitRepository;
import kr.co.lotteon.repository.cs.*;
import kr.co.lotteon.repository.member.MemberRepository;
import kr.co.lotteon.repository.member.TermsRepository;
import kr.co.lotteon.repository.product.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.beans.Transient;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminService {

    private final BoardRepository boardRepository;
    private final OptionRepository optionRepository;
    private final TermsRepository termsRepository;
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final OrderItemRepository orderItemRepository;
    private final ArticleRepository articleRepository;
    private final RecruitRepository recruitRepository;

    private final ModelMapper modelMapper;


    @Value("${img.upload.path}")
    private String imgUploadPath;


    // 관리자 환경설정 기본환경 정보 - 약관 조회
    public Terms findByTerms() {
        return termsRepository.findById(1).get();
    }

    // 관리자 인덱스 주문 차트 조회
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

    // 관리자 인덱스 회원 가입 차트 조회
    public List<Map<String, Object>> selectMemberForChart() {
        log.info("월별 가입자 count 조회 Serv 1");
        List<Tuple> tuples = memberRepository.selectMemberForChart();
        log.info("월별 가입자 count 조회 Serv 2: " + tuples);

        // 총 가입 수를 저장할 변수
        AtomicLong totalMembers = new AtomicLong(0);

        // 조회 결과 List로 반환
        List<Map<String, Object>> jsonResult = tuples.stream()
                .map(tuple -> {
                    int year = tuple.get(0, Integer.class);
                    int month = tuple.get(1, Integer.class);
                    long count = tuple.get(2, long.class);

                    // 총 주문 수에 현재 월의 가입 수를 더함
                    totalMembers.addAndGet(count);

                    Map<String, Object> map = new HashMap<>();
                    map.put("month", month + "월");
                    map.put("count", count);
                    return map;
                })
                .collect(Collectors.toList());

        // 총 가입 수를 결과에 추가
        Map<String, Object> totalMap = new HashMap<>();
        totalMap.put("total", totalMembers.get());
        jsonResult.add(totalMap);

        log.info("월별 가입자 count 조회 Serv 3: " + jsonResult);
        return jsonResult;
    }

    // 관리자 인덱스 공지사항 조회
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

    // 관리자 인덱스 고객문의 조회
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

    // 관리자 주문 현황
    @Transactional
    public SellerOrderPageResponseDTO selectOrderList(AdminPageRequestDTO adminPageRequestDTO){
        log.info("관리자 주문 현황 Serv 1  ");
        Pageable pageable = adminPageRequestDTO.getPageable("no");
        // order, orderItem, product, option 정보 DB 조회
        Page<Tuple> results = orderItemRepository.selectOrderListAll(adminPageRequestDTO, pageable);
        log.info("관리자 주문 현황 Serv 3 : " + results.getContent().size());
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

                    if(strOpNos != null) {

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

        log.info("관리자 주문 현황 Serv 4 : " + dtoList);

        // total 값
        int total = (int) results.getTotalElements();

        // List<OrderListDTO>와 page 정보 리턴
        return SellerOrderPageResponseDTO.builder()
                .adminPageRequestDTO(adminPageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }
    // 관리자 주문 검색 현황
    @Transactional
    public SellerOrderPageResponseDTO searchOrderList(AdminPageRequestDTO adminPageRequestDTO){
        log.info("관리자 주문 검색 검색 Serv 1  ");
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
            }
        }

        // order, orderItem, product, option 정보 DB 조회
        Page<Tuple> results = orderItemRepository.searchOrderListAll(adminPageRequestDTO, pageable);
        log.info("관리자 주문 검색 Serv 3 : " + results.getContent().size());
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

                    if(strOpNos != null) {

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

    // 관리자 회사 소개 글 목록
    public AdminArticlePageResponseDTO selectArticle(String cate1, AdminPageRequestDTO adminPageRequestDTO){
        Pageable pageable = adminPageRequestDTO.getPageable("no");
        String keyword = adminPageRequestDTO.getKeyword();
        Page<Article> results = null;
        // 일반 조회일 때
        if(keyword == null || keyword.equals("") || keyword.equals("all")){
            results = articleRepository.selectArticleForAdmin(adminPageRequestDTO, pageable, cate1);

            // 검색 조회일 때
        }else {
            results = articleRepository.searchArticleForAdmin(adminPageRequestDTO, pageable, cate1);
        }

        List<ArticleDTO> dtoList = results.stream()
                .map(result -> modelMapper.map(result, ArticleDTO.class))
                .toList();

        // total 값
        int total = (int) results.getTotalElements();

        // List<OrderListDTO>와 page 정보 리턴
        return AdminArticlePageResponseDTO.builder()
                .adminPageRequestDTO(adminPageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }
    // 관리자 회사 소개 채용 글 목록
    public RecruitPageResponseDTO selectRecruit(AdminPageRequestDTO adminPageRequestDTO, RecruitDTO recruitDTO){
        Pageable pageable = adminPageRequestDTO.getPageable("no");
        String keyword = adminPageRequestDTO.getKeyword();
        Page<Recruit> results = null;

        // 전체 조회이면
        if(recruitDTO.getEmployment() == 8 && recruitDTO.getStatus() == 8){
            results = recruitRepository.selectRecruitForAdmin(adminPageRequestDTO, pageable);
            // 검색
        }else{
            results = recruitRepository.searchRecruitForAdmin(adminPageRequestDTO, pageable, recruitDTO);
        }

        List<RecruitDTO> dtoList = results.stream()
                .map(result -> modelMapper.map(result, RecruitDTO.class))
                .toList();

        // total 값
        int total = (int) results.getTotalElements();

        // List<OrderListDTO>와 page 정보 리턴
        return RecruitPageResponseDTO.builder()
                .adminPageRequestDTO(adminPageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }
    // 관리자 회사 소개 글쓰기
    public void insertArticle(MultipartFile thumb336, ArticleDTO articleDTO){
        log.info("회사 소개 글쓰기 Serv 1 : " + thumb336);
        log.info("회사 소개 글쓰기 Serv 2 : " + articleDTO);

        // 이미지 파일 등록 : 해당 디렉토리 없을 경우 자동 생성
        File file = new File(imgUploadPath);
        if (!file.exists()) {
            file.mkdir();
        }
        String path = file.getAbsolutePath();

        // 원본 파일 폴더 자동 생성
        String orgPath = path + "/orgImage";
        File orgFile = new File(orgPath);
        if (!orgFile.exists()) {
            orgFile.mkdir();
        }
        // 이미지 리사이징
        if (!thumb336.isEmpty()) {
            // 리사이징 함수 호출 - 새 이미지 저장
            String sName = imgResizing(thumb336, orgPath, path, 380, 240);
            // 파일 이름 DTO에 저장
            articleDTO.setThumb(sName);
        }

        // DTO -> Entity
        Article article = modelMapper.map(articleDTO, Article.class);
        log.info("회사 소개 글쓰기 Serv 3 : " + article.toString());

        // 상품 정보 DB 저장
        articleRepository.save(article);
    }

    /////  이미지 리사이징 //////////////////////////////////////////////////
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
}
