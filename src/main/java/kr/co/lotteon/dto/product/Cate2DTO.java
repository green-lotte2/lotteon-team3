package kr.co.lotteon.dto.product;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Cate2DTO {
    private int cate2;
    private int cate1;
    private String c2Name;
    private List<Cate3DTO> cate3DTOList;


}
