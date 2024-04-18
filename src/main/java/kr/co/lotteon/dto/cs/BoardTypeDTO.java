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

    private int typeNo;                 // 게시판 유형 번호
    private String cate;            // 게시판 유형이 속한 카테고리
    private String typeName;        // 게시판 유형의 이름
    private List<BoardDTO> boards;  // 해당 게시판 유형에 속한 게시글들(리스트)

    public BoardTypeEntity toEntity(){
        return BoardTypeEntity.builder()
                .typeNo(typeNo)
                .cate(cate)
                .typeName(typeName)
                .build();
    }
}
