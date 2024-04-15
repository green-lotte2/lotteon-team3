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
@Table(name = "product_cate1")
public class Cate1 {
    @Id
    private int cate1;
    private String c1Name;
    private String css;
}
