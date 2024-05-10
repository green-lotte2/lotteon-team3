package kr.co.lotteon.service.product;

import com.querydsl.core.Tuple;
import kr.co.lotteon.dto.admin.SellerOrderPageResponseDTO;
import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.dto.product.WishDTO;
import kr.co.lotteon.dto.product.WishPageRequestDTO;
import kr.co.lotteon.dto.product.WishPageResponseDTO;
import kr.co.lotteon.entity.product.Product;
import kr.co.lotteon.entity.product.Wish;
import kr.co.lotteon.repository.product.WishRepository;
import kr.co.lotteon.security.MyUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class WishService {

    private final WishRepository wishRepository;
    private final ModelMapper modelMapper;

    // 찜 목록 조회
    public WishPageResponseDTO selectWishList(WishPageRequestDTO wishPageRequestDTO){
        Pageable pageable = wishPageRequestDTO.getPageable("no");
        String uid = whoAmI();
        Page<Tuple> results = wishRepository.selectWishList(uid, pageable);

        List<WishDTO> dtoList = results.getContent()
                .stream()
                .map(tuple -> {

                    // Tuple -> Entity
                    Wish wish = tuple.get(0, Wish.class);
                    Product product = tuple.get(1, Product.class);

                    // Entity -> DTO
                    WishDTO wishDTO = modelMapper.map(wish, WishDTO.class);
                    ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);

                    // 할인된 가격
                    int disPrice = calculateDiscountedPrice(productDTO.getPrice(),productDTO.getDiscount());
                    wishDTO.setDisPrice(disPrice);

                    wishDTO.setProductDTO(productDTO);

                    return wishDTO;
                })
                .toList();

        // total 값
        int total = (int) results.getTotalElements();

        // List<WishDTO>와 page 정보 리턴
        return WishPageResponseDTO.builder()
                .wishPageRequestDTO(wishPageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }
    // 상품 보기 - 찜 여부 조회
    public int existsWish(int prodNo){
        String uid = whoAmI();
        Boolean wishExists = wishRepository.existsByUidAndProdNo(uid, prodNo);

        return wishExists ? 1 : 0;
    }
    // 상품 보기 - 찜 하기/ 해제
    @Transactional
    public ResponseEntity<?> changeWish(int prodNo, int wish){
        String uid = whoAmI();

        WishDTO wishDTO = new WishDTO();
        wishDTO.setProdNo(prodNo);
        wishDTO.setUid(uid);
        log.info("찜 Service uid : " + uid);
        log.info("찜 Service prodNo : " + prodNo);
        if(wish == 1){
            // wish 추가
            wishRepository.save(modelMapper.map(wishDTO, Wish.class));

        }else{
            // wish 삭제
            wishRepository.deleteByUidAndProdNo(uid, prodNo);
        }
        return ResponseEntity.ok().body(wishDTO);
    }
    // 찜 선택 삭제
    public ResponseEntity<?> wishDelete(List<Integer> wishNoList){
        log.info("찜 삭제 serv : " + wishNoList);

        for(int wishNo : wishNoList){
            wishRepository.deleteById(wishNo);
        }
        return ResponseEntity.ok().body("ok");
    }
    // 개별 삭제
    public ResponseEntity<?> wishDelete(int wishNo){
        wishRepository.deleteById(wishNo);
        return ResponseEntity.ok().body("ok");
    }
    // 사용자 정보 함수
    public String whoAmI(){
        // 현재 로그인 중인 사용자 정보 불러오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 로그인 중일 때 해당 사용자 id를 seller에 입력
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        String sellerId = userDetails.getMember().getUid();

        return sellerId;
    }
    // 할인된 가격 계산
    private int calculateDiscountedPrice(int price, int discount) {
        // 할인율 -> 소수점
        double discountRate = (double) discount / 100;

        // 할인된 가격 계산
        return (int) (price - (price * discountRate));
    }
}
