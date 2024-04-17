package kr.co.lotteon.entity.cs;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kr.co.lotteon.dto.cs.BoardTypeDTO;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cs_boardtype")
public class BoardTypeEntity {
    @Id
    private int typeNo;
    private String cate;
    private String typeName;

    public BoardTypeDTO toDTO(){
        return BoardTypeDTO.builder()
                .typeNo(typeNo)
                .cate(cate)
                .typeName(typeName)
                .build();
    }
}
