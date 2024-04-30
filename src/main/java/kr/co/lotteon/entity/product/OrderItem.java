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
@Table(name = "product_order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ordItemno;

    private int ordNo;
    private int prodNo;
    private String uid;
    private int count;
    private Integer opNo;
    @CreationTimestamp
    private LocalDateTime ordDate;
    private String ordStatus;
}
