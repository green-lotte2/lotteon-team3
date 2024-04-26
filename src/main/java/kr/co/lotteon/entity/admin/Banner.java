package kr.co.lotteon.entity.admin;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="banner")
public class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bno;
    private String bnName;
    private String thumb;
    private String cate;
    private String link;
    private String backcolor;

    private LocalDate beginDate;
    private LocalTime beginTime;
    private LocalDate endDate;
    private LocalTime endTime;
    private int activation;
}
