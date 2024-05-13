package kr.co.lotteon.dto.product;

import kr.co.lotteon.dto.cs.BoardDTO;
import kr.co.lotteon.dto.cs.BoardTypeDTO;
import kr.co.lotteon.dto.cs.CommentDTO;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProductQnaDTO {

    private BoardDTO boardDTO;
    private BoardTypeDTO boardTypeDTO;
    private CommentDTO commentDTO;
}
