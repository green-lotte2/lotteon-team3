package kr.co.lotteon.dto.product;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ReviewDTO {
    private int revNo;
    private String uid;
    private int ordNo;
    private int prodNo;
    private int ordItemno;
    private int rating;
    private LocalDateTime rdate;
    private String content;
    private String regip;
    private Integer thumb;
}
