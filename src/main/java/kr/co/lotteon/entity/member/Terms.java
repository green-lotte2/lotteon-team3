package kr.co.lotteon.entity.member;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="member_terms")
public class Terms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String terms; //일반회원 이용약관(일반회원일경우 필수)
    private String privacy; //개인정보 이용약관(공통 필수)
    private String finance; //전자금융 이용약관(공통 필수)
    private String location; //일반회원 위치정보 이용약관(선택)
    private String tax;//판매자 이용약관(판매자일경우 필수)
}