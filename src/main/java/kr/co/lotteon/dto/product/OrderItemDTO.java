package kr.co.lotteon.dto.product;

import kr.co.lotteon.entity.member.Point;
import kr.co.lotteon.entity.product.OrderItem;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
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

    private int cate1;
    private int cate2;
    private int cate3;
    private String descript;
    private List<OptionDTO> optionList;

    private int totalPricePerProduct; // 상품 개별 총 가격(할인적용가)

    private String company;
    private String prodName;
    private int price;
    private int discount;
    private String thumb3;


    public OrderItem toEntity(){
        return OrderItem.builder()
                .ordItemno(ordItemno)
                .ordNo(ordNo)
                .prodNo(prodNo)
                .uid(uid)
                .count(count)
                .opNo(opNo)
                .ordDate(ordDate)
                .ordStatus(ordStatus)
                .company(company)
                .prodName(prodName)
                .price(price)
                .discount(discount)
                .thumb3(thumb3)
                .totalPricePerProduct(totalPricePerProduct)
                .build();
    }
}

