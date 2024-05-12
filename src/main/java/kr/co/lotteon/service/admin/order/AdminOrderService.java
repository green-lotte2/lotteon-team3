package kr.co.lotteon.service.admin.order;

import com.querydsl.core.Tuple;
import kr.co.lotteon.dto.admin.AdminPageRequestDTO;
import kr.co.lotteon.dto.admin.OrderListDTO;
import kr.co.lotteon.dto.admin.SellerOrderPageResponseDTO;
import kr.co.lotteon.dto.product.OptionDTO;
import kr.co.lotteon.dto.product.OrderDTO;
import kr.co.lotteon.dto.product.OrderItemDTO;
import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.entity.product.Order;
import kr.co.lotteon.entity.product.OrderItem;
import kr.co.lotteon.entity.product.Product;
import kr.co.lotteon.repository.product.OptionRepository;
import kr.co.lotteon.repository.product.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service

public class AdminOrderService {

    private final OrderItemRepository orderItemRepository;
    private final OptionRepository optionRepository;
    private final ModelMapper modelMapper;

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
                case "done":
                    adminPageRequestDTO.setKeyword("구매확정");
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
}
