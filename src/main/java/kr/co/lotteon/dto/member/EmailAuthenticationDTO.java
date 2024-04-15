package kr.co.lotteon.dto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EmailAuthenticationDTO {
    private String to;
    private String subject;
    private String message;
}
