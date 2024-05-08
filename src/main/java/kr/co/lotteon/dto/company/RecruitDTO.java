package kr.co.lotteon.dto.company;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class RecruitDTO {
    private int rno;
    private String title;
    private String content;
    private String experience;
    @Builder.Default
    private int employment = 8;
    private String occupation;
    @Builder.Default
    private int status = 8;
}
