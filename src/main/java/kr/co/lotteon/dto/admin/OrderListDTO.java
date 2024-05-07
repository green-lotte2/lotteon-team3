package kr.co.lotteon.dto.admin;

import kr.co.lotteon.dto.product.OptionDTO;
import kr.co.lotteon.dto.product.OrderDTO;
import kr.co.lotteon.dto.product.OrderItemDTO;
import kr.co.lotteon.dto.product.ProductDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderListDTO {
    private OrderItemDTO orderItemDTO;
    private OrderDTO orderDTO;
    private ProductDTO productDTO;
    private List<OptionDTO> opList;
}
