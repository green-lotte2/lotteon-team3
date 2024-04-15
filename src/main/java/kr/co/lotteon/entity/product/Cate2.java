package kr.co.lotteon.entity.product;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "product_cate2")
public class Cate2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cate2;

    private int cate1;

    private String c2Name;
}