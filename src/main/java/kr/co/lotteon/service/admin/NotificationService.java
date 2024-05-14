package kr.co.lotteon.service.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.co.lotteon.component.SseEmitters;
import kr.co.lotteon.dto.cs.BoardDTO;
import kr.co.lotteon.entity.cs.BoardEntity;
import kr.co.lotteon.entity.member.Member;
import kr.co.lotteon.repository.cs.BoardRepository;
import kr.co.lotteon.repository.member.MemberRepository;
import kr.co.lotteon.repository.product.ProductRepository;
import kr.co.lotteon.security.MyUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationService {

    private final SseEmitters sseEmitters;
    private final BoardRepository boardRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    private final ModelMapper modelMapper;

    // 로그인 시 최초 연결 요청
    public ResponseEntity<SseEmitter> subscribe() {

        log.info("SSE Service 1 ");
        Member member = whoAmI();

        // 현재 사용자의 SseEmitter가 이미 존재하는지 확인 - confirm창 최초 한번만
        SseEmitter existingEmitter = SseEmitters.sseEmitters.get(member.getUid());

        // 연결 요청한 클라이언트의 SseEmitter 객체 생성 후 저장
        SseEmitter sseEmitter = new SseEmitter(60L * 1000 * 10); // 만료 시간 10분
        sseEmitters.add(member.getUid(), sseEmitter);

        // 해당 판매자의 상품번호 전부 조회
        List<Integer> prodNos = productRepository.selectProdNoForQna(member.getNick());
        log.info("상품번호 전부 조회 " + prodNos);

        long count = boardRepository.countSellerQna(prodNos);
        log.info("상품 문의 count : " + count);
        log.info("SSE Service sseEmitters " + sseEmitters);
        try {
            /*
                Emitter를 생성하고 나서 만료 시간까지 아무런 데이터도 보내지 않으면 재연결 요청시 503 Service Unavailable 에러가 발생
                -> 503 에러 방지를 위한 더미 데이터 전송
            */
            if (count >= 1) {
                if(existingEmitter == null) {
                    log.info("서비스 1 : " + count);
                    sseEmitter.send(SseEmitter.event()
                            .name("connect") // 해당 이벤트의 이름 지정 : View(Client)에서 해당 이름으로 이벤트를 받음
                            .data(member.getNick() + "님의 답변 대기 중인 문의가 " + count + "건 있습니다."));
                }else{
                    log.info("서비스 1-1 이미 보냄  : " + count);
                    sseEmitter.send(SseEmitter.event()
                            .name("connect")
                            .data("none"));
                }
            } else {
                log.info("서비스 2 : " + count);
                sseEmitter.send(SseEmitter.event()
                        .name("connect")
                        .data("none"));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("SSE Service 2 ");


        // SseEmitter::complete : 연결 종료
        sseEmitter.onTimeout(sseEmitter::complete);
        sseEmitter.onError((e) -> sseEmitter.complete());

        log.info("SSE Service 3 ");
        return ResponseEntity.ok().body(sseEmitter);

    }

    // 문의글 작성되면 해당 판매자에게 알람
    private void send(String sellerId) {
        log.info("문의 판매자에게 알람 1 ");
        SseEmitter sseEmitter = SseEmitters.sseEmitters.get(sellerId);
        if (sseEmitter != null) {
            log.info("send to 판매자 sseEmitter : " + sseEmitter.toString());
            try {
                log.info("send to 판매자 : " + sellerId);
                // 이벤트 데이터 전송
                sseEmitter.send(SseEmitter.event()
                        .name("send")
                        .data("새로 작성된 상품 문의가 있습니다."));

            } catch (IllegalStateException e) {
                log.error("IllegalStateException is occurred. ", e);
            } catch (IOException e) {
                log.error("IOException is occurred. ", e);
            }
        }
    }

    // 상품 문의 작성
    @Transactional
    public ResponseEntity<?> prodQnaSave(BoardDTO boardDTO) {

        log.info("상품 문의 작성 Serv 1 : " + boardDTO);
        boardDTO.setUid(whoAmI().getUid());
        boardDTO.setCate("product");
        boardDTO.setStatus("검토중");
        boardDTO.setGroup("qna");

        BoardEntity board = modelMapper.map(boardDTO, BoardEntity.class);
        boardRepository.save(board);
        log.info("상품 문의 작성 Serv 2 : " + board);
        // 문의 알람 전송
        String sellerId = memberRepository.selectUidByProdNo(boardDTO.getProdNo());
        send(sellerId);

        return ResponseEntity.ok().body(board);
    }

    // 사용자 정보 함수
    public Member whoAmI() {
        // 현재 로그인 중인 사용자 정보 불러오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 로그인 중일 때 해당 사용자 id를 seller에 입력
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        Member seller = userDetails.getMember();

        return seller;
    }
}
