package kr.co.lotteon.dto.product;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OptionListDTO {
    // 상품 상세 옵션
    private List<OptionDTO> optionList;
}
