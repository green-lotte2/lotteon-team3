package kr.co.lotteon.dto.product;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OptionDTO {
    // 상품 상세 옵션
    private int opNo;
    private int prodNo;
    private String opName;
    private String opValue;

}
