package kr.co.lotteon.entity.member;

import jakarta.persistence.*;
import kr.co.lotteon.dto.member.point.PointDTO;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="member_point")
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pointNo;
    private String uid;
    private int ordNo;
    private int point;
    private String descript;

    @CreationTimestamp
    private LocalDateTime pointDate;
    private String usecase;

    @CreationTimestamp
    private LocalDateTime currentDate;

    public PointDTO toDTO(){
        return PointDTO.builder()
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