package kr.co.lotteon.dto.product;

import lombok.*;

import java.util.List;

// 상품 리스트 페이징 처리를 위한 PageResponseDTO
@Getter @Setter @ToString @AllArgsConstructor
public class PageResponseDTO {
    private List<ProductDTO> dtoList;
    private int pg;
    private int size;
    private int total;
    private int cate1;
    private int cate2;
    private int cate3;
    private String sort;
    private String how;

    private int start, end;
    private boolean prev, next;

    @Builder
    public PageResponseDTO(PageRequestDTO pageRequestDTO, List<ProductDTO> dtoList, int total){

        this.pg = pageRequestDTO.getPg();
        this.size = pageRequestDTO.getSize();
        this.total = total;
        this.cate1 = pageRequestDTO.getCate1();
        this.cate2 = pageRequestDTO.getCate2();
        this.sort = pageRequestDTO.getSort();
        this.how = pageRequestDTO.getHow();
        this.dtoList = dtoList;

        this.end = (int) (Math.ceil(this.pg / 10.0)) * 10;
        this.start = this.end -9;

        int last = (int) (Math.ceil(total / (double)size));
        this.end = end > last ? last : end;
        this.prev = this.start > 1;
        this.next = total > this.end * this.size;
    }
}
