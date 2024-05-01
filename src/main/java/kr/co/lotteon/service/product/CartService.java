package kr.co.lotteon.service.product;

import kr.co.lotteon.dto.product.CartDTO;
import kr.co.lotteon.dto.product.CartInfoDTO;
import kr.co.lotteon.entity.product.Cart;
import kr.co.lotteon.repository.product.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service @Slf4j @RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;

    // 카트 넣기
    public void insertCart(CartDTO cartDTO){
        // 장바구니에 해당 상품이 있는지 확인
        List<Cart> onCartItems = cartRepository.findCartByUidAndProdNo(cartDTO.getUid(), cartDTO.getProdNo());
        log.info("cartService 0 "+onCartItems);
        // 장바구니에 상품이 없으면 추가

            if(onCartItems.isEmpty()){
                log.info("cartService 1");

               Cart cart = new Cart();
               cart.setProdNo(cartDTO.getProdNo());
               cart.setUid(cartDTO.getUid());
               cart.setCount(cartDTO.getCount());
               cart.setOpNo(cartDTO.getOpNo());
               cartRepository.save(cart);
            }
            else{
                log.info("cartService 2");
                // 해당 상품이 장바구니에 있으면 수량 추가
                for(Cart onCartItem : onCartItems){
                    if(onCartItem.getOpNo().equals(cartDTO.getOpNo())){
                        log.info("db의 옵션 : " + onCartItem.getOpNo());
                        log.info("받아오는 옵션 : " + cartDTO.getOpNo());

                        int newCount = onCartItem.getCount() + cartDTO.getCount();
                        onCartItem.setCount(newCount);
                        cartRepository.save(onCartItem);
                        log.info("cartService 3" + onCartItem);
                        break;
                    }else{
                        log.info("db의 옵션 : " + onCartItem.getOpNo());
                        log.info("받아오는 옵션 : " + cartDTO.getOpNo());
                        Cart cart = new Cart();
                        cart.setProdNo(cartDTO.getProdNo());
                        cart.setUid(cartDTO.getUid());
                        cart.setCount(cartDTO.getCount());
                        cart.setOpNo(cartDTO.getOpNo());
                        cartRepository.save(cart);
                        log.info("cartService 4" + onCartItem);
                        break;
                    }

            }

        }
    }


    // 장바구니 조회하기
    public List<CartInfoDTO> selectCartProduct(String uid){
        List<CartInfoDTO> result = cartRepository.selectCartProduct(uid);
        log.info("CartService {}",result);
        return result;
    }

    // 장바구니에 담긴 회사 조회
    public List<String> selectCartCompany (String uid){
        return cartRepository.selectCartCompany(uid);
    }

    // 장바구니 삭제
    public ResponseEntity<?> deleteCart(int[] cartNoArray){
        log.info("장바구니 삭제 서비스" + Arrays.toString(cartNoArray));

        for (int cartNo : cartNoArray) {
            // 상품 삭제 반복
            cartRepository.deleteById(cartNo);
        }
        return  ResponseEntity.ok().body("ok");
    }
}
