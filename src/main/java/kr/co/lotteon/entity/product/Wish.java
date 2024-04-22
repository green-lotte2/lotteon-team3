package kr.co.lotteon.entity.product;

import jakarta.persistence.*;
import kr.co.lotteon.entity.member.Member;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "wish")
public class Wish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int wishNo;
    @ManyToOne
    @JoinColumn(name = "uid")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "prodNo")
    private Product product;
}