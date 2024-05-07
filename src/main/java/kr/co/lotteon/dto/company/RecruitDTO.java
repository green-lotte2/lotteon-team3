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
    private int employment;
    private String occupation;
    private int status;
}
