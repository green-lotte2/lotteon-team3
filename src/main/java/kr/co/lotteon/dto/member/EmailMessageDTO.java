package kr.co.lotteon.dto.member;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmailMessageDTO {
    private String to;
    private String subject;
    private String message;
}
