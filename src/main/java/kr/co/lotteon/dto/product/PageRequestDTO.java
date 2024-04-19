package kr.co.lotteon.dto.product;

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageRequestDTO{

    @Builder.Default
    private int pg = 1;

    @Builder.Default
    private int size=10;

    @Builder.Default
    private int cate1 = 10;

    @Builder.Default
    private int cate2 = 11;

    @Builder.Default
    private String how = "DESC";

    @Builder.Default
    private String sort = "sold";

    private String searchType;
    private String searchKeyword;

    public Pageable getPageable() {
        if (this.how.equals("ASC")) {
            return PageRequest.of(this.pg - 1, this.size, Sort.by(this.sort).descending());
        } else {
            return PageRequest.of(this.pg - 1, this.size, Sort.by(this.sort).descending());
        }
    }
}