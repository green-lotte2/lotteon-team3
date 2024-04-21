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
@Table(name = "product_cate3")
public class Cate3 {
    @Id
    private int cate3;

    private int cate2;

    private String c3Name;
}