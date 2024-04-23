package kr.co.lotteon.entity.cs;

import jakarta.persistence.*;
import kr.co.lotteon.dto.cs.BoardFileDTO;
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
@Table(name = "cs_boardfile")
public class BoardFileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int fno;
    private int bno;
    private String ofile;
    private String sfile;

    @ColumnDefault("0")
    private int download;

    @CreationTimestamp
    private LocalDateTime rdate;

    // 파일 경로 저장 필드 추가
    private String filePath;

    public BoardFileDTO toDTO(){
        return BoardFileDTO.builder()
                .fno(fno)
                .bno(bno)
                .ofile(ofile)
                .sfile(sfile)
                .download(download)
                .rdate(rdate)
                .filePath(filePath)
                .build();
    }
}