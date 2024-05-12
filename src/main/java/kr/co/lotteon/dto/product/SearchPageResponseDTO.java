package kr.co.lotteon.dto.product;

import lombok.*;

import java.util.List;

// 상품 리스트 페이징 처리를 위한 PageResponseDTO
@Getter @Setter @ToString @AllArgsConstructor
public class SearchPageResponseDTO {
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
    public SearchPageResponseDTO(SearchPageRequestDTO searchPageRequestDTO, List<ProductDTO> dtoList, int total){

        this.pg = searchPageRequestDTO.getPg();
        this.size = searchPageRequestDTO.getSize();
        this.total = total;
        this.cate1 = searchPageRequestDTO.getCate1();
        this.cate2 = searchPageRequestDTO.getCate2();
        this.sort = searchPageRequestDTO.getSort();
        this.how = searchPageRequestDTO.getHow();
        this.dtoList = dtoList;

        this.end = (int) (Math.ceil(this.pg / 10.0)) * 10;
        this.start = this.end -9;

        int last = (int) (Math.ceil(total / (double)size));
        this.end = end > last ? last : end;
        this.prev = this.start > 1;
        this.next = total > this.end * this.size;
    }
}
