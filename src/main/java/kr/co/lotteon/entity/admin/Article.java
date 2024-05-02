package kr.co.lotteon.entity.admin;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="article")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ano;
    private String title;
    private String content;
    private String cate1;
    private String cate2;
    private String thumb;

    @CreationTimestamp
    private LocalDateTime rdate;
}
