package kr.co.lotteon.dto.product;

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
public class ReviewDTO {
    private int revNo;
    private String uid;
    private int ordNo;
    private int prodNo;
    private int ordItemno;
    private String optionValue;
    private int rating;
    private int cate1;
    private int cate2;
    private LocalDateTime rdate;
    private String content;
    private String regip;
    private Integer thumb;

    // join을 위한
    private String nick;
    private String prodName;
    private List<OptionDTO> optionList;
}
