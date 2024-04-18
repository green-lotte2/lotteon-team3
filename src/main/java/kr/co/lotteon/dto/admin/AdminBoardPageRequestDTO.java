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
public class AdminBoardPageRequestDTO {
    @Builder.Default
    private int no = 1;
    @Builder.Default
    private int pg = 1;
    @Builder.Default
    private int size = 10;

    @Builder.Default
    private String group = "notice";    // 그룹 문자열

    private String type; // 작성자, 카테고리 등 검색 타입
    private String keyword;

    public Pageable getPageable(String sort){
        return PageRequest.of(this.pg - 1, this.size, Sort.by(sort).descending());
    }
}
