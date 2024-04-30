package kr.co.lotteon.entity.product;

import jakarta.persistence.*;
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
@Table(name = "product_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ordNo;

    private String ordUid;
    private Integer couponSeq;
    private int disCouponPrice;
    private int ordComplete;
    private int ordCount;
    private int ordDelivery;
    private int ordDiscount;
    private int ordPayment;
    private int ordPrice;
    private int ordTotPrice;
    private int savePoint;
    private int usedPoint;
    @CreationTimestamp
    private LocalDateTime ordDate;
    private String ordUser;
    private String recipAddr1;
    private String recipAddr2;
    private String recipHp;
    private String recipName;
    private String recipZip;
}
