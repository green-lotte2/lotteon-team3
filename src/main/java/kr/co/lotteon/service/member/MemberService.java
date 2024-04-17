package kr.co.lotteon.service.member;


import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import kr.co.lotteon.dto.member.MemberDTO;
import kr.co.lotteon.entity.member.Member;
import kr.co.lotteon.mapper.MemberMapper;
import kr.co.lotteon.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final JavaMailSender javaMailSender;
    
    // 회원 가입 - DB 입력
    public void save(MemberDTO memberDTO){
        // 비밀번호 암호화
        memberDTO.setPass1(passwordEncoder.encode(memberDTO.getPass1())); 
        Member member = modelMapper.map(memberDTO, Member.class);
        memberRepository.save(member); 
    }

    public int selectCountUser(String Datatype, String value) {
        return memberMapper.selectCountUser(Datatype, value);
    }

    @Value("${spring.mail.username}")
    private String sender;

    public void sendEmailCode(HttpSession session, String receiver){

        log.info("sender : " + sender);

        // MimeMessage 생성
        MimeMessage message = javaMailSender.createMimeMessage();

        // 인증코드 생성 후 세션 저장
        int code = ThreadLocalRandom.current().nextInt(100000, 1000000);
        session.setAttribute("code", String.valueOf(code));

        log.info("code : " + code);

        String title = "lotteon 인증코드 입니다.";
        String content = "<h1>인증코드는 " + code + "입니다.</h1>";

        try {
            message.setFrom(new InternetAddress(sender, "보내는 사람", "UTF-8"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
            message.setSubject(title);
            message.setContent(content, "text/html;charset=UTF-8");

            javaMailSender.send(message);

        }catch(Exception e){
            log.error("sendEmailConde : " + e.getMessage());
        }

    }
}
