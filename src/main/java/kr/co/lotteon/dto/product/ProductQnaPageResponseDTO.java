package kr.co.lotteon.dto.product;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductQnaPageResponseDTO {
    private List<ProductQnaDTO> dtoList;
    private int pg;
    private int size;
    private int total;
    private int start, end;
    private int last;
    private boolean prev, next;

    @Builder
    public ProductQnaPageResponseDTO(ProductReviewPageRequestDTO productReviewPageRequestDTO, List<ProductQnaDTO> dtoList, int total) {
        this.pg = productReviewPageRequestDTO.getPg();
        this.size = productReviewPageRequestDTO.getSize();
        this.total = total;
        this.dtoList = dtoList;
        this.end = (int) (Math.ceil(this.pg/10.0)) * 10;
        this.start = this.end - 9;
        this.last = (int) (Math.ceil(total/(double)size));
        this.end = Math.min(end, last);
        this.prev = this.start > 1;
        this.next = total > this.end * this.size;
    }
}