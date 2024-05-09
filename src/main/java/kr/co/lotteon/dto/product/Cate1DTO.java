package kr.co.lotteon.dto.product;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Cate1DTO {
    private int cate1;
    private String c1Name;
    private String css;
    private List<Cate2DTO> cate2;

   /* // cate2
    private Cate2DTO cate2;
    private Cate2DTO c2Name;*/

    /*// cate3
    private Cate2DTO cate3;
    private Cate2DTO c3Name;*/
}
