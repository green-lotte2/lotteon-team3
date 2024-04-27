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
public class CartDTO {
    private int cartNo;
    private String uid;
    private int prodNo;
    private int count;
    private String opNo;
    private LocalDateTime rdate;
}
