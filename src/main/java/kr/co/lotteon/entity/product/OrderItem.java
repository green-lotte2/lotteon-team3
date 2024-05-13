package kr.co.lotteon.entity.product;

import jakarta.persistence.*;
import kr.co.lotteon.dto.product.OrderItemDTO;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "product_order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ordItemno;

    private int ordNo;
    private int prodNo;
    private String uid;
    private int count;
    private String opNo;
    @CreationTimestamp
    private LocalDateTime ordDate;
    private String ordStatus;


    @Transient
    private int totalPricePerProduct;
    @Transient
    private String company;
    @Transient
    private String prodName;
    @Transient
    private int price;
    @Transient
    private int discount;
    @Transient
    private String thumb3;

    public OrderItemDTO toDTO(){
        return OrderItemDTO.builder()
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

