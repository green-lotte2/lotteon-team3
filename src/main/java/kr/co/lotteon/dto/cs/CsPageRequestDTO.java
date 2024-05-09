package kr.co.lotteon.dto.cs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class CsPageRequestDTO {

    @Builder.Default
    private int pg =1;                  // 페이지 번호
    @Builder.Default
    private int size = 10;              // 한 페이지당 표시되는 번호 수

    private String cate;
    // 카테고리 문자열
    @Builder.Default
    private String group = "notice";    // 그룹 문자열

    private int type;                   // 게시글의 유형

    public Pageable getPageable(String sort){           // 페이지 요청 정보 생성, 정렬 방식 설정
        return PageRequest.of(this.pg-1, this.size, Sort.by(sort).descending());
    }

}