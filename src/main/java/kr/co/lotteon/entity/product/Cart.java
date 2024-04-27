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
@Table(name = "product_cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartNo;

    private String uid;
    private int prodNo;
    private int count;
    private String opNo;

    @CreationTimestamp
    private LocalDateTime rdate;

}
