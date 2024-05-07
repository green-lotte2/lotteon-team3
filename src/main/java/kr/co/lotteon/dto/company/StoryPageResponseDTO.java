package kr.co.lotteon.dto.company;

import kr.co.lotteon.dto.admin.ArticleDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StoryPageResponseDTO {
    private List<ArticleDTO> dtoList;
    private int pg;
    private int size;
    private int total;
    private int startNo;
    private int last;
    private String cate2;

    private int start, end;
    private boolean prev, next;

    @Builder
    public StoryPageResponseDTO(StoryPageRequestDTO storyPageRequestDTO, List<ArticleDTO> dtoList, int total){

        this.pg = storyPageRequestDTO.getPg();
        this.size = storyPageRequestDTO.getSize();
        this.total = total;
        this.dtoList = dtoList;
        this.cate2 = storyPageRequestDTO.getCate2();

        this.startNo = total - ((pg - 1) * size);
        this.end = (int) (Math.ceil(this.pg/10.0))*10;
        this.start = this.end - 9;

        int last = (int) (Math.ceil(total / (double) size));
        this.last = last;
        this.end = end > last ? last : end;
        this.prev = this.start > 1;
        this.next = total > this.end * this.size;
    }
}
