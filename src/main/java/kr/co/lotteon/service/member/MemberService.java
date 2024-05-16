package kr.co.lotteon.service.member;


import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import kr.co.lotteon.dto.member.CouponDTO;
import kr.co.lotteon.dto.member.MemberDTO;
import kr.co.lotteon.entity.member.Coupon;
import kr.co.lotteon.entity.member.Member;
import kr.co.lotteon.entity.member.Terms;
import kr.co.lotteon.mapper.MemberMapper;
import kr.co.lotteon.repository.member.MemberRepository;
import kr.co.lotteon.repository.member.TermsRepository;
import kr.co.lotteon.repository.my.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
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
    private final TermsRepository termsRepository;
    private final CouponRepository couponRepository;

    // 회원 가입 - DB 입력
    @Transactional
    public void saveMember(MemberDTO memberDTO, CouponDTO couponDTO){
        // 비밀번호 암호화
        memberDTO.setPass(passwordEncoder.encode(memberDTO.getPass()));
        Member member = modelMapper.map(memberDTO, Member.class);
        memberRepository.save(member);

        // 가입시 쿠폰
        Coupon coupon = modelMapper.map(couponDTO, Coupon.class);
        couponRepository.save(coupon);
    }
    public void save(MemberDTO memberDTO){
        // 비밀번호 암호화
        memberDTO.setPass(passwordEncoder.encode(memberDTO.getPass()));
        Member member = modelMapper.map(memberDTO, Member.class);
        memberRepository.save(member);
    }

    // 가입시 쿠폰
    public void insertCoupon (CouponDTO couponDTO){

    }
    public int selectCountMember(String type, String value) {
        return memberMapper.selectCountMember(type, value);
    }

    public int CountByNameAndEmail(String type, String value, String email){
        return memberMapper.countByNameAndEmail(type,value,email);
    }

    public MemberDTO findAllByEmail(String email) {

        Member member=memberRepository.findAllByEmail(email);

        return modelMapper.map(member, MemberDTO.class);
    }

    public void updatePass(String uid, String pass) {
        pass = passwordEncoder.encode(pass);
        Member member = memberRepository.findById(uid).get();
        member.setPass(pass);
        log.info("비밀번호 변경되었니");
        memberRepository.save(member);
    }

    public MemberDTO findByUid(String uid){

        Optional<Member> member=memberRepository.findById(uid);
        return modelMapper.map(member, MemberDTO.class);
    }

    //회원탈퇴
    public void updateWdate(String uid) {
        Member member = memberRepository.findById(uid).get();
        member.setWdate(LocalDateTime.now());
        memberRepository.save(member);
    }

    //myInfo - 닉네임 조회
    public Optional<Member> selectMemberByUidAndNickname(String uid, String nick) {
        return memberRepository.selectMemberByUidAndNickname(uid,nick);
    }
    //myInfo - 닉네임 저장
    public void updateNick(String uid, String nick) {
        Member member = memberRepository.findById(uid).get();
        member.setNick(nick);
        log.info("닉네임 변경완료");
        memberRepository.save(member);
    }

    //myInfo - 이메일 일치여부
    public int countByUidAndEmail(String uid, String email){
        return memberRepository.countByUidAndEmail(uid,email);
    }
    
    //myInfo - 이메일 수정
    public void updateEmail(String uid, String email){
        Member member = memberRepository.findById(uid).get();
        member.setEmail(email);
        log.info("이메일 변경 완료");
        memberRepository.save(member);
    }

    //myInfo - 휴대폰 중복검사
    public int countByUidAndHp(String uid,String hp){
        return memberRepository.countByUidAndHp(uid,hp);
    }


    //myInfo - 휴대폰 수정
    public void updateHp(String uid,String hp){
        Member member = memberRepository.findById(uid).get();
        member.setHp(hp);
        log.info("휴대폰 변경 완료");
        memberRepository.save(member);
    }

    //myInfo - 주소 수정
    public void updateAddr(String uid,String zip,String addr1, String addr2){
        Member member = memberRepository.findById(uid).get();
        member.setZip(zip);
        member.setAddr1(addr1);
        member.setAddr2(addr2);
        log.info("주소 변경 완료");
        memberRepository.save(member);
    }

    // 약관 출력
    public Terms findByTerms(){
        return termsRepository.findById(1).get();
    }

    //이메일 전송
    @Value("${spring.mail.username}")
    private String sender;
    public void sendEmailCode(HttpSession session, String receiver) {
        log.info("sender={}", sender);

        //MimeMessage 생성
        MimeMessage message = javaMailSender.createMimeMessage();

        //인증코드 생성 후 세션 저장
        String code = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        session.setAttribute("code", code);

        log.info("code={}", code);

//        String title = "[롯데ON] 회원가입 인증번호 안내드립니다";
//        String content = "<h1>인증코드는 " + code + "입니다.<h1>";

        //html 불러와서 메일 내용에 삽입할 예정
        String title = "[롯데ON] 인증번호 안내드립니다";
        String content = "<div>\n" +
                "    <table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-family: 'Noto Sans KR','맑은 고딕','Malgun Gothic','Roboto','돋움','Dotum','Helvetica','Apple SD Gothic Neo', sans-serif; margin: 0 auto; padding: 0 32px; width: 656px;\">";
        content+="<tbody><tr><td style=\"margin: 0; padding: 0;\">\n" +
                "            <table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"656\" style=\"font-family: 'Noto Sans KR','맑은 고딕','Malgun Gothic','Roboto','돋움','Dotum','Helvetica','Apple SD Gothic Neo', sans-serif;\">\n" +
                "                <tbody><tr><td cellpadding=\"0\" cellspacing=\"0\" border=\"0\" height=\"1\" style=\"line-height: 1px; margin: 0; min-width: 656px; padding: 0;\">\n" +
                "                </td></tr>\n" +
                "                </tbody></table>\n" +
                "        </td></tr>\n" +
                "        <tr><td height=\"36\" style=\"margin: 0; padding: 0;\"></td></tr>\n" +
                "        <tr><td style=\"margin: 0; padding: 0;\">\n" +
                "            <img src=\"https://contents.lotteon.com/email/email_image/fo/resources/assets/common-bi-lotteon-header-re2.png\" height=\"88\" width=\"656\" usemap=\"#on\" alt=\"롯데온 로고\" loading=\"lazy\">\n" +
                "            <map name=\"on\">\n" +
                "                <area target=\"_blank\" shape=\"rect\" coords=\"0,14,200,65\" alt=\"lotte on\" href=\"https://tmsapi.lotteon.com/msg-api/tracking?TV9JRD1NQl85OTk5OV81MTAwNzYyMQ==&amp;U1RZUEU9QVVUTw==&amp;p_id=20240418_4&amp;m_id=MB_99999_51007621&amp;s_tp=AUTO&amp;TElTVF9UQUJMRT1UTVNfQVVUT19TRU5EX0xJU1RfMDE=&amp;UE9TVF9JRD0yMDI0MDQxOF80&amp;U0VSVkVSX0lEPTAy&amp;VEM9MjAyNDA0MjU=&amp;S0lORD1D&amp;Q0lEPTAwMQ==&amp;c_id=001&amp;msg_type=01&amp;msg_type_seq=89&amp;enc_email=e83e79c5ab78faea0e7dde0ecdc53ad627477c9069f209f958965f90a24b36f5&amp;URL=https://www.lotteon.com/display/main/lotteon?ch_no=100005&amp;ch_dtl_no=1033000\">\n" +
                "            </map>\n" +
                "        </td></tr>\n" +
                "        <tr><td height=\"20\" style=\"margin: 0; padding: 0;\"></td></tr>\n" +
                "        </tbody></table>\n" +
                "    <table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-family: 'Noto Sans KR','맑은 고딕','Malgun Gothic','Roboto','돋움','Dotum','Helvetica','Apple SD Gothic Neo', sans-serif; margin: 0 auto; padding: 0 32px; width: 656px;\">\n" +
                "        <tbody><tr><td style=\"margin: 0; padding: 0;\">\n" +
                "\n" +
                "\n" +
                "            <table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-family: 'Noto Sans KR','맑은 고딕','Malgun Gothic','Roboto','돋움','Dotum','Helvetica','Apple SD Gothic Neo', sans-serif; margin: 0 auto; width: 656px;\">\n" +
                "                <tbody><tr><td height=\"64\" style=\"margin: 0; padding: 0;\"></td></tr>\n" +
                "                <tr><td align=\"left\" style=\"margin: 0; padding: 0;\">\n" +
                "                        <span style=\"color: #333; font-family: 'Noto Sans KR', 'Malgun Gothic', '돋움','Dotum','Apple SD Gothic Neo', 'Roboto', sans-serif; font-size: 44px; letter-spacing: -0.5px; line-height: 62px;\">이메일 회원 가입<br>\n" +
                "                            인증번호 안내드립니다.</span>\n" +
                "                </td></tr>\n" +
                "                <tr><td height=\"40\" style=\"margin: 0; padding: 0;\"></td></tr>\n" +
                "                <tr><td align=\"left\" style=\"margin: 0; padding: 0;\">\n" +
                "                        <span style=\"color: #757575; font-family: 'Noto Sans KR', 'Malgun Gothic', '돋움','Dotum','Apple SD Gothic Neo', 'Roboto', sans-serif; font-size: 26px; letter-spacing: -0.2px; line-height: 38px; text-align: left;\">전송된 아래 인증번호를<br>\n" +
                "                        회원가입 페이지에 입력하시면<br>\n" +
                "                            롯데ON 이메일 인증이 완료됩니다.</span>\n" +
                "                </td></tr>\n" +
                "                <tr><td height=\"48\" style=\"border-bottom: 2px solid #eee; margin: 0; padding: 0;\"></td></tr>\n" +
                "                </tbody></table>\n" +
                "\n" +
                "        </td></tr>\n" +
                "        <tr><td style=\"margin: 0; padding: 0;\">\n" +
                "\n" +
                "\n" +
                "            <table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-family: 'Noto Sans KR','맑은 고딕','Malgun Gothic','Roboto','돋움','Dotum','Helvetica','Apple SD Gothic Neo', sans-serif; margin: 0 auto; width: 656px;\">\n" +
                "                <tbody><tr><td height=\"96\" style=\"margin: 0; padding: 0;\"></td></tr>\n" +
                "                <tr><td style=\"color: #333; font-size: 30px; margin: 0; padding: 0; text-align: left;\">\n" +
                "                    <b style=\"color: #333; font-family: 'Noto Sans KR', 'Malgun Gothic', '돋움','Dotum','Apple SD Gothic Neo', 'Roboto', sans-serif;\">인증 번호 정보</b>\n" +
                "                </td></tr>\n" +
                "                <tr><td height=\"20\" style=\"border-bottom: 2px solid #333; margin: 0; padding: 0;\"></td></tr>\n" +
                "                </tbody></table>\n" +
                "\n" +
                "        </td></tr>\n" +
                "        <tr><td style=\"margin: 0; padding: 0;\">\n" +
                "\n" +
                "\n" +
                "            <table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-family: 'Noto Sans KR','맑은 고딕','Malgun Gothic','Roboto','돋움','Dotum','Helvetica','Apple SD Gothic Neo', sans-serif; margin: 0 auto; width: 656px;\">\n" +
                "                <tbody><tr><td height=\"32\" colspan=\"2\" style=\"margin: 0; padding: 0;\"></td></tr>\n" +
                "                <tr><td height=\"16\" colspan=\"2\" style=\"margin: 0; padding: 0;\"></td></tr>\n" +
                "                <tr><td width=\"252\" height=\"42\" style=\"margin: 0; padding: 0; text-indent: 16px;\"><span style=\"color: #757575; font-family: 'Noto Sans KR', 'Malgun Gothic', '돋움','Dotum','Apple SD Gothic Neo', 'Roboto', sans-serif; font-size: 28px; letter-spacing: -0.3px; line-height: 38px; text-align: left;\">인증번호</span></td><td width=\"404\" height=\"42\" style=\"margin: 0; padding: 0;\"><span style=\"color: #333; font-family: 'Noto Sans KR', 'Malgun Gothic', '돋움','Dotum','Apple SD Gothic Neo', 'Roboto', sans-serif; font-size: 28px; letter-spacing: -0.3px; line-height: 38px; text-align: left;\">"+code+"</span></td></tr>\n" +
                "                <tr><td colspan=\"2\" height=\"32\" style=\"border-bottom: 2px solid #eee; margin: 0; padding: 0;\"></td></tr>\n" +
                "                </tbody></table>\n" +
                "\n" +
                "        </td></tr>\n" +
                "        </tbody></table>\n" +
                "    <table align=\"center\" width=\"360\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-family: 'Noto Sans KR','맑은 고딕','Malgun Gothic','Roboto','돋움','Dotum','Helvetica','Apple SD Gothic Neo', sans-serif;\">\n" +
                "        <tbody><tr><td height=\"144\" colspan=\"2\" style=\"margin: 0; padding: 0;\"></td></tr>\n" +
                "        </tbody></table></div>\n" +
                "</body>\n" +
                "</html>";

        try {
            log.info("이메일 보내지니?");
            message.setSubject(title);
            message.setFrom(new InternetAddress(sender, "보내는 사람", "UTF-8"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
            message.setSubject(title);
            message.setContent(content, "text/html;charset=UTF-8");


            javaMailSender.send(message);

        } catch (Exception e) {
            log.error("error={}", e.getMessage());
        }

    }
}
