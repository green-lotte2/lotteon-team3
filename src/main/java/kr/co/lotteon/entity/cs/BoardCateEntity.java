package kr.co.lotteon.entity.cs;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kr.co.lotteon.dto.cs.BoardCateDTO;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "cs_boardcate")
public class BoardCateEntity {

    @Id
    private String cate;
    private String cateName;

    public BoardCateDTO toDTO(){
        return BoardCateDTO.builder()
                .cate(cate)
                .cateName(cateName)
                .build();
    }
}
