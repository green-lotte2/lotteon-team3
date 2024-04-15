package kr.co.lotteon.dto.cs;

import kr.co.lotteon.entity.cs.BoardCateEntity;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardCateDTO {
    private String cate;                    // 카테고리 식별
    private String cateName;                // 카테고리 이름

    private List<BoardTypeDTO> type;        // 게시판의 유형(일반, 공지사항, 이벤트 등)

    public BoardCateEntity toEntity(){
        return BoardCateEntity.builder()
                .cate(cate)
                .cateName(cateName)
                .build();
    }
}
