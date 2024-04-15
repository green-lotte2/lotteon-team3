package kr.co.lotteon.dto.product;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ReplyDTO {
    private int repNo;
    private int revNo;
    private String uid;
    private String content;
    private LocalDateTime rdate;
}
