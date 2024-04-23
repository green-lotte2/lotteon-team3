package kr.co.lotteon.dto.cs;

import kr.co.lotteon.entity.cs.BoardFileEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class BoardFileDTO {
    private int fno;
    private int bno;
    private String ofile;
    private String sfile;
    private int download;

    private LocalDateTime rdate;

    // 파일 경로 저장 필드 추가
    private String filePath;

    public BoardFileEntity toEntity(){
        return BoardFileEntity.builder()
                .fno(fno)               // 파일번호
                .bno(bno)               // 파일이 속한 게시글 번호
                .ofile(ofile)           // 원본 파일 이름
                .sfile(sfile)           // 서버에 저장된 파일 이름
                .download(download)     // 다운로드 횟수
                .rdate(rdate)           // 파일 생성 날짜
                .filePath(filePath)
                .build();
    }
}