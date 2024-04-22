package kr.co.lotteon.dto.member;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PointDTO {
    private int pointNo;
    private String uid;
    private int ordNo;
    private int point;
    private String descript;
    private LocalDateTime pointDate;
    private String usecase;


}
