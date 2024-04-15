package kr.co.lotteon.dto.cs;

import kr.co.lotteon.entity.cs.BoardTypeEntity;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardTypeDTO {

    private int no;                 // 게시판 유형 번호
    private String cate;            // 게시판 유형이 속한 카테고리
    private int type;               // 게시판의 유형(게시판 종류)
    private String typeName;        // 게시판 유형의 이름
    private List<BoardDTO> boards;  // 해당 게시판 유형에 속한 게시글들(리스트)

    public BoardTypeEntity toEntity(){
        return BoardTypeEntity.builder()
                .no(no)
                .cate(cate)
                .type(type)
                .typeName(typeName)
                .build();
    }
}
