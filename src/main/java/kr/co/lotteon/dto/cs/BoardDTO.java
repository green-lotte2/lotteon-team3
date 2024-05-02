package kr.co.lotteon.dto.cs;

import kr.co.lotteon.dto.member.MemberDTO;
import kr.co.lotteon.entity.cs.BoardEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class BoardDTO {
    private int bno;                // 게시글 번호
    private String uid;             // 게시글 작성자 ID
    private Integer prodNo;
    private String group;           // 게시글이 속한 그룹
    private String cate;            // 게시글이 속한 카테고리
    private int typeNo;               // 게시글의 유형(일반, 공지사항, 이벤트 등)
    private String title;           // 게시글 제목
    private String content;         // 게시글 내용


    private int file;               // 첨부된 파일 개수
    private int hit;                // 조회수
    private String status;          // 게시글의 상태(게시, 비공개, 삭제)
    private int reply;              // 게시글의 답변 개수

    private LocalDateTime rdate;    // 작성 날짜

    private List<MultipartFile> files;

    private MultipartFile fname;    // 첨부된 파일
    private String typeName;        // 게시글 유형
    private String cateName;        // 게시글이 속한 카테고리
    private int index;              // 게시글의 인덱스
    private String nick;            // 작성자의 닉네임

    private List<BoardFileDTO> fileDTOList;     // 게시글에 첨부된 파일들(리스트)

    public BoardEntity toEntity(){
        return BoardEntity.builder()
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