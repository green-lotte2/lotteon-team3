package kr.co.lotteon.dto.admin;

import lombok.*;

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
public class BannerDTO {
    private int bno;
    private String thumb;
    private String bnName;
    private String cate;
    private String link;
    private String backcolor;

    private LocalDate beginDate;
    private LocalTime beginTime;
    private LocalDate endDate;
    private LocalTime endTime;

    @Builder.Default
    private int activation = 1;
}
