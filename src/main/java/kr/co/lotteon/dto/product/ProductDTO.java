package kr.co.lotteon.dto.product;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProductDTO {
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

    // 주문용
    private int count;
    private String opNo;

    List<OptionDTO> optionList;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime rdate;

}
