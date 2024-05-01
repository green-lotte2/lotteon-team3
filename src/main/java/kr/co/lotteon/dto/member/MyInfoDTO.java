package kr.co.lotteon.dto.member;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyInfoDTO {
    private int myPoint;
    private int couponCount;
    private int qnaCount;
    private int orderCount;
}