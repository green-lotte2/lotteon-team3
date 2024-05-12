package kr.co.lotteon.dto.product;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderItemDTO {
    private int ordItemno;

    private int ordNo;
    private int prodNo;
    private String uid;
    private int count;
    private String opNo;
    private LocalDateTime ordDate;
    private String ordStatus;

    // 상품과 조인

    private String prodName;
    private String company;
    private String descript;
    private int price;
    private int discount;
    private String thumb3;
    private int cate1;
    private int cate2;
    private int cate3;

    private int totalPricePerProduct; // 상품 개별 총 가격(할인적용가)

    // 옵션 리스트
    private List<OptionDTO> optionList;
}
