package kr.co.lotteon.dto.product;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class CartInfoDTO {

    // product
    private int prodNo;
    private int cate1;
    private int cate2;
    private int cate3;
    private int delivery;
    private int discount;
    private int hit;
    private int point;
    private int price;
    private int review;
    private int score;
    private int sold;
    private int stock;
    private int amount;

    private String bizType;
    private String company;
    private String deleteYn;
    private String descript;
    private String detail;
    private String duty;
    private String ip;
    private String origin;
    private String prodName;
    private String receipt;
    private String seller;
    private String status;
    private String thumb1;
    private String thumb2;
    private String thumb3;
    private String rdate;

    // option
    private String opName;
    private String opValue;

    //cart
    private int count;
    private String opNo;

}
