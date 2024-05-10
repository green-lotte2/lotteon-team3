package kr.co.lotteon.entity.member;

import jakarta.persistence.*;
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
@Table(name="member_coupon")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int couponSeq;
    private String uid;
    private String couponName;
    private int discountType;
    private int discountLimit;
    private int discountMoney;
    private int discountPercent;
    @Builder.Default
    private String useYn = "Y"; //쿠폰 사용가능여부(Y : 사용가능, N : 사용완료)
    @CreationTimestamp
    private LocalDateTime rdate;
    private LocalDateTime expireDate;
}