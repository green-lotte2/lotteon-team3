package kr.co.lotteon.dto.member.point;

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString

@Data
@Component
public class PointPageRequestDTO {
    private String begin;
    private String end;

    @Builder.Default
    private int pg =1;                  // 페이지 번호
    @Builder.Default
    private int size = 10;              // 한 페이지당 표시되는 번호 수

    public Pageable getPageable(){           // 페이지 요청 정보 생성
        return PageRequest.of(this.pg-1, this.size);
    }

}
