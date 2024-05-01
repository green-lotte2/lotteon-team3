package kr.co.lotteon.dto.member.point;

import kr.co.lotteon.entity.member.Point;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PointDTO {
    private int pointNo;
    private String uid;
    private int ordNo;
    private int point;
    private String descript;
    private LocalDateTime pointDate;
    private String usecase;
    private LocalDateTime currentDate;

    public Point toEntity(){
        return Point.builder()
                .pointNo(pointNo)
                .uid(uid)
                .ordNo(ordNo)
                .point(point)
                .descript(descript)
                .pointDate(pointDate)
                .usecase(usecase)
                .currentDate(currentDate)
                .build();
    }
}