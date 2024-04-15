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
@Table(name = "product_review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int revNo;

    private String uid;
    private int ordNo;
    private int prodNo;
    private int ordItemno;
    private int rating;
    @CreationTimestamp
    private LocalDateTime rdate;
    private String content;
    private String regip;
    private Integer thumb;
}
