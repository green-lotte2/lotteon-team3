package kr.co.lotteon.service.product;

import kr.co.lotteon.dto.product.WishDTO;
import kr.co.lotteon.entity.product.Wish;
import kr.co.lotteon.repository.product.WishRepository;
import kr.co.lotteon.security.MyUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class WishService {

    private final WishRepository wishRepository;
    private final ModelMapper modelMapper;

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

    // 사용자 정보 함수
    public String whoAmI(){
        // 현재 로그인 중인 사용자 정보 불러오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 로그인 중일 때 해당 사용자 id를 seller에 입력
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        String sellerId = userDetails.getMember().getUid();

        return sellerId;
    }
}
