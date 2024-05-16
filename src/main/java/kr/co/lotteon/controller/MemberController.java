package kr.co.lotteon.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.co.lotteon.dto.admin.BannerDTO;
import kr.co.lotteon.dto.member.CouponDTO;
import kr.co.lotteon.dto.member.MemberDTO;
import kr.co.lotteon.entity.member.Terms;
import kr.co.lotteon.service.admin.BannerService;
import kr.co.lotteon.service.member.MemberService;
import kr.co.lotteon.service.member.TermsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final TermsService termsService;
    private final MemberService memberService;
    private final BannerService bannerService;

    // signup (약관 동의) 페이지 매핑
    @GetMapping("/member/signup")
    public String signup(Model model,@RequestParam String type){

        log.info("약관동의 type = "+type);

        //type로 일반회원(normal)인지 판매자(seller)인지 구분
        model.addAttribute("type",type);
        Terms terms = termsService.findByTerms();
        model.addAttribute("terms",terms);

        return "/member/signup";
    }


    // join (회원 가입 구분) 페이지 매핑
    @GetMapping("/member/join")
    public String join(){
        return "/member/join";
    }

    // login 페이지 매핑
    @GetMapping("/member/login")
    public String login(Model model, @ModelAttribute("success") String success, HttpServletRequest request){

        // 로그인 배너
        List<BannerDTO> loginBanners = bannerService.selectBanners("login");

        model.addAttribute("loginBanners",loginBanners);

        return "/member/login";
    }

    // register 페이지 매핑
    @PostMapping("/member/join")
    public String register(Model model, String type,String location){

        log.info("회원가입 type = "+type);
        log.info("동의 "+location);

        model.addAttribute("type", type);
        model.addAttribute("location", location);

        // type이 판매자(seller)로 들어오면 판매자 회원가입 페이지로 리다이렉트
        if(type.equals("seller")){
            return "redirect:/member/registerSeller?type=seller";
        }
        
        return "/member/register";
    }

    // 회원가입시 email 중복검사 후 인증메일 전송
    @ResponseBody
    @GetMapping("/member/check/{type}/{value}")
    public ResponseEntity<?> checkUser(HttpSession session,
                                       @PathVariable("type") String type,
                                       @PathVariable("value") String value) {

        log.info("type={}", type);
        log.info("value={}", value);
        int count = memberService.selectCountMember(type, value);

        log.info("count={}", count);

        if (type.equals("email") && count <= 0) {
            log.info("회원가입 email={}", value);
            memberService.sendEmailCode(session, value);
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("result", count);

        return ResponseEntity.ok().body(resultMap);
    }

    //아이디 비밀번호찾기
    @ResponseBody
    @GetMapping("/member/check/{type}/{value}/{email}")
    public ResponseEntity<?> checkFindUser(HttpSession session,
                                       @PathVariable("type") String type,
                                       @PathVariable("value") String value,
                                       @PathVariable("email") String email){
        
        log.info("findIdEmail들어와야해 "+ type);
        log.info("이름 아디 입력한거 "+ value);
        log.info("이멜 입력한거 "+ email);

        int count = memberService.CountByNameAndEmail(type, value,email);

        log.info("일치하는 행의 수 "+ count);

        if(type.equals("findIdEmail") && count > 0){
            log.info("findIdEmail "+value+email);
            memberService.sendEmailCode(session, email);
        }

        if(type.equals("findPassEmail") && count > 0){
            log.info("findPassEmail "+value+email);
            memberService.sendEmailCode(session, email);
        }


        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("result", count);

        return ResponseEntity.ok().body(resultMap);
    }



    //이메일 인증코드 검사
    @ResponseBody
    @GetMapping("/member/email/{code}")
    public ResponseEntity<?> checkEmailCode(HttpSession session, @PathVariable("code") String code) {
        String sessionCode = (String) session.getAttribute("code");

        Map<String, Object> resultMap = new HashMap<>();
        
        log.info("이메일 인증코드 검사 : "+sessionCode.equals(code));

        if (sessionCode.equals(code)) {
            resultMap.put("result", true);
        } else {
            resultMap.put("result", false);
        }
        return ResponseEntity.ok().body(resultMap);
    }



    // 회원 가입 처리 - DB 전송
    @PostMapping("/member/register")
    public String register(MemberDTO memberDTO, HttpServletRequest request){

        LocalDateTime currentDate = LocalDateTime.now();
        // 현재 날짜에서 5일을 더해서 만료 날짜 설정
        LocalDateTime expireDateTime = currentDate.plusDays(5);

        log.info("PASSWORD "+memberDTO.getPass());

        // 신규 회원시 5천원 할인 쿠폰
        CouponDTO couponDTO = new CouponDTO();
        couponDTO.setUid(memberDTO.getUid());
        couponDTO.setDiscountLimit(5000);
        couponDTO.setDiscountMoney(5000);
        couponDTO.setCouponName("신규 회원 5000원 쿠폰");
        couponDTO.setExpireDate(expireDateTime);
        couponDTO.setDiscountType(30000);
        couponDTO.setUseYn("Y");

        memberDTO.setLevel(1); // 일반회원시 level 1
        memberDTO.setRegip(request.getRemoteAddr());
        memberDTO.setLocation(memberDTO.getLocation());

        log.info("memberDTO"+memberDTO);
        memberService.saveMember(memberDTO, couponDTO);

        return "redirect:/member/login?success=200";
    }

    // registerSeller (판매자 가입) 페이지 매핑
    @GetMapping("/member/registerSeller")
    public String registerSeller(){

        return "/member/registerSeller";
    }

    @PostMapping("/member/registerSeller")
    public String registerSeller(MemberDTO memberDTO,HttpServletRequest request){
        memberDTO.setRegip(request.getRemoteAddr());
        memberDTO.setLevel(5); // 일반회원시 level 5
        memberDTO.setName(memberDTO.getCompany());
        memberDTO.setNick(memberDTO.getCompany());
        memberService.save(memberDTO);

        return "redirect:/member/login?success=200";
    }

    @GetMapping("/member/findId")
    public String findId(){

        return "/member/findId";
    }

    @GetMapping("/member/findIdResult")
    public String findIdResult() {

        return "/member/findIdResult";
    }

    @PostMapping("/member/findIdResult")
    public String findIdResult(Model model,String email) {
        MemberDTO memberDTO = memberService.findAllByEmail(email);
        model.addAttribute("memberDTO",memberDTO);

        return "/member/findIdResult";
    }

    @GetMapping("/member/findPass")
    public String findPass(){

        return "/member/findPass";
    }

    @GetMapping("/member/findPassChange")
    public String findPassChange(){

        return "/member/findPassChange";
    }

    @PostMapping("/member/findPassChange")
    public String findPassChange(Model model,String email){

        MemberDTO memberDTO = memberService.findAllByEmail(email);
        model.addAttribute("memberDTO",memberDTO);

        return "/member/findPassChange";
    }

    @PostMapping("/member/findPassChangeDo")
    public String findPassChangeDo(MemberDTO memberDTO,Model model) {
        memberService.updatePass(memberDTO.getUid(), memberDTO.getPass());
        return "redirect:/member/login";
    }




}
