package kr.co.lotteon.dto.admin;

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class AdminProductPageRequestDTO {
    @Builder.Default
    private int no = 1;
    @Builder.Default
    private int pg = 1;
    @Builder.Default
    private int size = 10;

    private String type; //상품명, 상품코드, 상품분류, 제조사, 판매자
    private String keyword;

    public Pageable getPageable(String sort){
        return PageRequest.of(this.pg - 1, this.size, Sort.by(sort).descending());
    }
}
