package kr.co.lotteon.service.product;

import kr.co.lotteon.dto.product.CartDTO;
import kr.co.lotteon.dto.product.CartInfoDTO;
import kr.co.lotteon.dto.product.OptionDTO;
import kr.co.lotteon.entity.product.Cart;
import kr.co.lotteon.repository.product.CartRepository;
import kr.co.lotteon.repository.product.OptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service @Slf4j @RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final OptionRepository optionRepository;
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

        //for문 돌려서 opNO 뽑아서 option List 넣기
        for(CartInfoDTO cartInfoDTO : result){
            String opNos = cartInfoDTO.getOpNo();
            log.info("옵션 뽑아오기 1: "+opNos);

            if (opNos != null && !opNos.isEmpty()) {
                // ,로 옵션 문자열 분리하기
                String[] optionString = opNos.split(",");
                int[] optionIds = new int[optionString.length];

                for (int i = 0; i < optionString.length; i++) {
                    optionIds[i] = Integer.parseInt(optionString[i].trim());
                }
                log.info("옵션뽑아오기 2" + Arrays.toString(optionIds));

                List<OptionDTO> options = new ArrayList<>();

                for(int opNo : optionIds){
                    OptionDTO option = optionRepository.selectOptionForCart(opNo);
                    log.info("옵션뽑아오기 3" + option);

                    if (option != null){
                        options.add(option);
                    }
                }
                cartInfoDTO.setDtoList(options);
            }
        }

        log.info("카트 서비스 : " + result);

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

    public ResponseEntity<?> updateCartCount(CartDTO cartDTO){
        log.info("카트서비스 1" + cartDTO);

        Optional<Cart> result = cartRepository.findById(cartDTO.getCartNo());
        log.info("카트서비스 2" + result);

        if(result.isPresent()){
           Cart cart = new Cart();
           cart.setCartNo(cartDTO.getCartNo());
           cart.setProdNo(result.get().getProdNo());
           cart.setUid(result.get().getUid());
           cart.setCount(cartDTO.getCount());
           cart.setOpNo(result.get().getOpNo());
           cart.setUid(result.get().getUid());
           cart.setRdate(result.get().getRdate());
           log.info("카트서비스 3" + cart);
           cartRepository.save(cart);
           return ResponseEntity.ok().body("ok");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NOT FOUND");
        }
    }
}
