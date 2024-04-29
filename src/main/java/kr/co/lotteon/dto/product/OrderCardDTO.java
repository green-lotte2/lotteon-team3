package kr.co.lotteon.dto.product;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderCardDTO {
    Long orderCount;
    Integer orderSum;
}
