package kr.co.lotteon.service.product;

import groovy.util.logging.Slf4j;
import kr.co.lotteon.dto.product.CartDTO;
import kr.co.lotteon.entity.product.Cart;
import kr.co.lotteon.repository.product.CartRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    // 카트 넣기
    public void insertCart(CartDTO cartDTO){
        List<Cart> onCartItem = cartRepository.findCartByUidAndProdNo(cartDTO.getUid(), cartDTO.getProdNo());

        if(onCartItem.isEmpty()){

           Cart cart = new Cart();
           cart.setProdNo(cartDTO.getProdNo());
           cart.setUid(cartDTO.getUid());
           cart.setCount(cartDTO.getCount());
           cartRepository.save(cart);
        }

    }
}
