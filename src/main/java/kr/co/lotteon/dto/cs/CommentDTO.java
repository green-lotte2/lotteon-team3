package kr.co.lotteon.dto.cs;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private int cno;
    private int bno;
    private String uid;

    private String group;
    private String cate;
    private String content;
    private LocalDateTime rdate;
    private String nick;
}