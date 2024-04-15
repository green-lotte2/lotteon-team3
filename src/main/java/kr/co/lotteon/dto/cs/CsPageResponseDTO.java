package kr.co.lotteon.dto.cs;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class CsPageResponseDTO {

    private List<BoardDTO> dtoList;         // 게시글 목록을 담고있는 BoardDTO 객체의 리스트
    private String cate;                    // 카테고리
    private String group;                   // 그룹
    private int pg;                         // 현재 페이지 번호

    private int size;                       // 한 페이지 표시 번호
    private int total;                      // 전체 항목 수
    private int start, end;                 // 시작 페이지, 끝나는 페이지
    private boolean prev, next;             // 이전 페이지, 다음 페이지

    // 생성자 - CsPageRequestDTO, 게시글 목록(dtoList), 전체 항목 수(total)를 받아서 페이지 응답 DTO 객체를 생성
    @Builder
    public CsPageResponseDTO(CsPageRequestDTO csPageRequestDTO, List<BoardDTO> dtoList, int total){
        this.cate = csPageRequestDTO.getCate();
        this.group = csPageRequestDTO.getGroup();
        this.pg = csPageRequestDTO.getPg();
        this.size = csPageRequestDTO.getSize();
        this.total = total;
        this.dtoList = dtoList;

        this.end = (int) (Math.ceil(this.pg/10.0)) * 10;
        this.start = this.end - 9;
        int last = (int) (Math.ceil(total/(double)size));

        this.end = Math.min(end, last);
        this.prev = this.start > 1;
        this.next = total > this.end * this.size;
    }
}