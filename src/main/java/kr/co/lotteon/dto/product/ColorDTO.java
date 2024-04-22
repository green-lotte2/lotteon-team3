package kr.co.lotteon.dto.product;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ColorDTO {
    // 의류 상품 상세 옵션
    private int opStock;
    private String color;
    private String colorName;
    private String size;
}
