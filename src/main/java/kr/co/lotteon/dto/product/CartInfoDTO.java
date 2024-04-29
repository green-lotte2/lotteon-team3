package kr.co.lotteon.dto.product;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class CartInfoDTO {

    // product
    private String prodName;
    public int delivery;
    public int discount;
    public int price;
    public String thumb1;
    public String company;
    public String descript;

    // option
    private String opName;
    private String opValue;

    //cart
    private int count;
    private int prodNo;
    private String opNo;

}
