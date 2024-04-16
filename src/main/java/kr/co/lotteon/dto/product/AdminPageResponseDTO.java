package kr.co.lotteon.dto.product;

import lombok.*;

import java.util.List;

// 상품 리스트 페이징 처리를 위한 PageResponseDTO
@Getter @Setter @ToString @AllArgsConstructor
public class AdminPageResponseDTO {
    private List<ProductDTO> dtoList;
    private int pg;
    private int size;
    private int total;
    private int startNo;
    private String type;
    private String keyword;

    private int start, end;
    private boolean prev, next;

    @Builder
    public AdminPageResponseDTO(AdminPageRequestDTO adminPageRequestDTO, List<ProductDTO> dtoList, int total){

        this.pg = adminPageRequestDTO.getPg();
        this.size = adminPageRequestDTO.getSize();
        this.total = total;
        this.dtoList = dtoList;
        this.type = adminPageRequestDTO.getType();
        this.keyword = adminPageRequestDTO.getKeyword();

        this.startNo = total - ((pg - 1) * size);
        this.end = (int) (Math.ceil(this.pg/10.0))*10;
        this.start = this.end - 9;

        int last = (int) (Math.ceil(total / (double) size));
        this.end = end > last ? last : end;
        this.prev = this.start > 1;
        this.next = total > this.end * this.size;
    }
}
