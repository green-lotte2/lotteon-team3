package kr.co.lotteon.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@Slf4j
public class SseEmitters {

    public static Map<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    /*
    SseEmitter를 생성할 때는 비동기 요청이 완료되거나 타임아웃 발생 시 실행할 콜백을 등록할 수 있습니다.
    타임아웃이 발생하면 브라우저에서 재연결 요청을 보내는데, 이때 새로운 Emitter 객체를
    다시 생성하기 때문에(SseController의 connect()메서드 참조) 기존의 Emitter를 제거해주어야 합니다.
    따라서 onCompletion 콜백에서 자기 자신을 지우도록 등록합니다.

    주의할 점은 이 콜백이 SseEmitter를 관리하는 다른 스레드에서 실행된다는 것입니다.
    따라서 thread-safe한 자료구조를 사용하지 않으면 ConcurrnetModificationException이 발생할 수 있습니다.
    여기서는 thread-safe한 자료구조인 CopyOnWriteArrayList를 사용하였습니다.
     */
    public SseEmitter add(String uid, SseEmitter emitter) {
        this.sseEmitters.put(uid, emitter);
        log.info("new emitter added: {}", emitter);
        log.info("emitter list size: {}", sseEmitters.size());

        emitter.onCompletion(() -> {
            log.info("만료 콜백");
            this.sseEmitters.remove(emitter);    // 만료되면 리스트에서 삭제
        });
        emitter.onTimeout(() -> {
            log.info("onTimeout callback");
            emitter.complete();
        });

        return emitter;
    }
}