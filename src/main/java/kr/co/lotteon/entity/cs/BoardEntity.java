package kr.co.lotteon.entity.cs;

import jakarta.persistence.*;
import kr.co.lotteon.dto.cs.BoardDTO;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "cs_board")
public class BoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bno;
    private String uid;
    @Column(name = "prodNo", nullable = true)
    private Integer prodNo;
    
    @Column(name = "\"group\"")
    private String group;
    private String cate;
    private int typeNo;
    private String title;
    private String content;

    @Builder.Default
    private Integer file = -1;

    @ColumnDefault("0")
    private int hit;

    private String status;
    private int reply;

    @CreationTimestamp
    private LocalDateTime rdate;

    // join용 변수 생성
    @Transient
    private String typeName;

    public BoardDTO toDTO(){
        return BoardDTO.builder()
                .bno(bno)
                .uid(uid)
                .group(group)
                .cate(cate)
                .typeNo(typeNo)
                .title(title)
                .content(content)
                .file(file)
                .status(status)
                .reply(reply)
                .rdate(rdate)
                .build();

    }
}