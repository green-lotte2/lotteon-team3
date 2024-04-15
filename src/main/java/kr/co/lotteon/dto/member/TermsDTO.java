package kr.co.lotteon.dto.member;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TermsDTO {
    private int id;
    private String terms;
    private String privacy;
    private String finance;
    private String location;
    private String tax;
}