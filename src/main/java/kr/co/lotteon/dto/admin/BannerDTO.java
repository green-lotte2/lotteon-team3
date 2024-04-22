package kr.co.lotteon.dto.admin;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BannerDTO {
    private int bno;
    private String thumb;
    private String cate;
    private LocalDate startdate;
    private LocalTime starttime;
}
