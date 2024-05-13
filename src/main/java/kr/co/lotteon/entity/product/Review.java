package kr.co.lotteon.entity.product;

import jakarta.persistence.*;
import kr.co.lotteon.dto.product.OptionDTO;
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

    // join을 위한
    @Transient
    private String nick;
    @Transient
    private String prodName;
    @Transient
    private List<OptionDTO> optionList;
}
