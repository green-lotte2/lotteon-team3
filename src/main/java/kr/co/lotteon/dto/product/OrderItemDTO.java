package kr.co.lotteon.dto.product;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

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

    private String company;
    private String prodName;
    private int price;
    private int discount;
    private String thumb3;

    private int totalPricePerProduct; // 상품 개별 총 가격(할인적용가)
}
