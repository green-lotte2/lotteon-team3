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
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int prodNo;
    private int cate1;
    private int cate2;
    private int cate3;

    private int delivery;
    private int discount;
    private int hit;
    private int point;
    private int price;
    private int review;
    private int score;
    private int sold;
    private int stock;
    private int amount;

    private String bizType;
    private String company;
    private String deleteYn;
    private String descript;
    private String detail;
    private String duty;
    private String ip;
    private String origin;
    private String prodName;
    private String receipt;
    private String seller;
    private String status;
    private String thumb1;
    private String thumb2;
    private String thumb3;

    @CreationTimestamp
    private LocalDateTime rdate;
}
