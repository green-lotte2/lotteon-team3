package kr.co.lotteon.dto.member;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponDTO {
    private int couponSeq;
    private String uid;
    private String couponName;
    private int discountType;
    private int discountLimit;
    private int discountMoney;
    private int discountPercent;
    private String useYn;
    private LocalDateTime rdate;
    private LocalDateTime expireDate;

    private String useYnString; //쿠폰 사용가능여부

    public void changeUseYnString() {
        this.useYnString = this.useYn.equals("Y") ? "사용가능" : "사용완료";
    }

}