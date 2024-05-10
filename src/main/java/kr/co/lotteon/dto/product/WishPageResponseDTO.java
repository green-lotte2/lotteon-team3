package kr.co.lotteon.dto.product;

import kr.co.lotteon.dto.admin.AdminProductPageRequestDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WishPageResponseDTO {
    private List<WishDTO> dtoList;
    private int pg;
    private int size;
    private int total;
    private int startNo;
    private String type;
    private String keyword;

    private int start, end;
    private boolean prev, next;

    @Builder
    public WishPageResponseDTO(WishPageRequestDTO wishPageRequestDTO, List<WishDTO> dtoList, int total){

        this.pg = wishPageRequestDTO.getPg();
        this.size = wishPageRequestDTO.getSize();
        this.total = total;
        this.dtoList = dtoList;

        this.startNo = total - ((pg - 1) * size);
        this.end = (int) (Math.ceil(this.pg/10.0))*10;
        this.start = this.end - 9;

        int last = (int) (Math.ceil(total / (double) size));
        this.end = end > last ? last : end;
        this.prev = this.start > 1;
        this.next = total > this.end * this.size;
    }
}
