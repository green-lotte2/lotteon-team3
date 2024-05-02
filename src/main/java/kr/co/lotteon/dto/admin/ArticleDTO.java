package kr.co.lotteon.dto.admin;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleDTO {

    private int ano;
    private String title;
    private String content;
    private String cate1;
    private String cate2;
    private String thumb;

    private LocalDateTime rdate;

}
