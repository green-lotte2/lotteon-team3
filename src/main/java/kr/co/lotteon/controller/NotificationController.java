package kr.co.lotteon.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.lotteon.service.admin.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@Slf4j
@RequiredArgsConstructor
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    // SSE 연결 요청 client -> Server
    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect(HttpServletRequest httpServletRequest) {
        log.info("SSE Controller 1 ");
        return notificationService.subscribe();
    }

    @GetMapping(value = "/notification", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> seller(){
        log.info(" 판매자 111 cont ");
        return null;
    }
}
