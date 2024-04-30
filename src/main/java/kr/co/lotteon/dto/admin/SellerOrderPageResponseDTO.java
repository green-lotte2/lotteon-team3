package kr.co.lotteon.dto.admin;

import kr.co.lotteon.dto.cs.BoardDTO;
import kr.co.lotteon.dto.product.OrderItemDTO;
import kr.co.lotteon.entity.product.OrderItem;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SellerOrderPageResponseDTO {
    private List<OrderListDTO> dtoList;
    private int pg;
    private int size;
    private int total;
    private int startNo;
    private String group;
    private String type;
    private String keyword;

    private int start, end;
    private boolean prev, next;

    @Builder
    public SellerOrderPageResponseDTO(AdminPageRequestDTO adminPageRequestDTO, List<OrderListDTO> dtoList, int total){

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
