package kr.co.lotteon.entity.product;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "optin")
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int opNo;

    @ManyToOne
    @JoinColumn(name = "prodNo")
    private Product product;

    private String opName;
    private String opValue;
}
