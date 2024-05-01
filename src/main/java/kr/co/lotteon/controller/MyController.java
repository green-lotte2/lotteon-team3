package kr.co.lotteon.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.co.lotteon.dto.admin.BannerDTO;
import kr.co.lotteon.dto.cs.BoardDTO;
import kr.co.lotteon.dto.member.CouponDTO;
import kr.co.lotteon.dto.member.MemberDTO;
import kr.co.lotteon.dto.member.MyInfoDTO;
import kr.co.lotteon.entity.member.Member;
import kr.co.lotteon.security.MyUserDetails;
import kr.co.lotteon.service.admin.BannerService;
import kr.co.lotteon.service.member.MemberService;
import kr.co.lotteon.service.my.MyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MyController {

    private final BannerService bannerService;
    private final MemberService memberService;
    private final MyService myService;
    private final AuthenticationManager authenticationManager;

    // my - home (마이페이지 메인) 페이지 매핑
    @GetMapping("/my/home")
    public String home(Model model, @RequestParam String uid){
        log.info("uid : "+uid);

        // 마이페이지 배너
        List<BannerDTO> myPageBanners = bannerService.selectBanners("myPage");

        model.addAttribute("myPageBanners",myPageBanners);
        return "/my/home";
    }
    // my - info (나의 설정) 페이지 매핑
    @GetMapping("/my/info")
    public String info(Model model,@RequestParam String uid){

        MemberDTO memberDTO =memberService.findByUid(uid);
        model.addAttribute("memberDTO",memberDTO);

        return "/my/info";
    }
    @PostMapping("/my/info")
    public String info(MemberDTO changeMemberDTO,@RequestParam String uid){
        log.info("PASSWORD "+changeMemberDTO.getPass());

        log.info("changeMemberDTO"+changeMemberDTO);
        MemberDTO memberDTO = memberService.findByUid(uid);
        memberDTO.setPass(changeMemberDTO.getPass());
        memberDTO.setNick(changeMemberDTO.getNick());
        memberDTO.setEmail(changeMemberDTO.getEmail());
        memberDTO.setHp(changeMemberDTO.getHp());
        memberDTO.setZip(changeMemberDTO.getZip());
        memberDTO.setAddr1(changeMemberDTO.getAddr1());
        memberDTO.setAddr2(changeMemberDTO.getAddr2());

        memberService.save(memberDTO);

        return "redirect:/index?success=200";
    }

    @ResponseBody
    @PostMapping("/my/withdraw")
    public String withdraw(@RequestParam String uid, String inputPass) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(uid, inputPass);
        Authentication result = authenticationManager.authenticate(authentication);

        if (result.isAuthenticated()) {
            memberService.updateWdate(uid);
            return "success";
        } else {
            return "fail";
        }
    }


    // my - order (나의 전체 주문내역) 페이지 매핑
    @GetMapping("/my/order")
    public String order(){
        return "/my/order";
    }
    // my - point (나의 포인트) 페이지 매핑
    @GetMapping("/my/point")
    public String point(){
        return "/my/point";
    }
    // my - coupon 페이지 매핑
    @GetMapping("/my/coupon")
    public String coupon(Model model,@RequestParam String uid){

        List<CouponDTO> coupons = myService.findCouponsByUid(uid);

        int count = myService.findCouponCountByUidAndUseYn(uid);

        model.addAttribute("coupons",coupons);
        model.addAttribute("count",count);

        log.info("내 쿠폰"+coupons);
        log.info("count"+count);


        return "/my/coupon";
    }

    @GetMapping("/my/myInfo")
    @ResponseBody
    public MyInfoDTO myInfo(@AuthenticationPrincipal Object principal) {
        Member memberEntity = ((MyUserDetails) principal).getMember();
        String uid = memberEntity.getUid();
        int couponCount = myService.findCouponCountByUidAndUseYn(uid);
        log.info("쿠폰의 수"+couponCount);
        int orderCount = myService.findOrderByUidAndOrdStatus(uid);
        log.info("주문의 수"+orderCount);
        int qnaCount = myService.findQnaByUidAndStatus(uid);
        log.info("문의의 수"+qnaCount);
        return MyInfoDTO.builder()
                .myPoint(memberEntity.getPoint())
                .couponCount(couponCount)
                .orderCount(orderCount)
                .qnaCount(qnaCount)
                .build();
    }




    // my - qna (마이페이지 문의하기) 페이지 매핑
    @GetMapping("/my/qna")
    public String qna(Model model,@RequestParam String uid){

        List<BoardDTO> boards = myService.findByBoardAndUid(uid);
        int count = myService.countByUid(uid);

        model.addAttribute("boards",boards);
        model.addAttribute("count",count);

        log.info("내 문의 : "+boards);
        log.info("내 문의 수 : "+count);

        return "/my/qna";
    }
    // my - review (나의 리뷰내역) 페이지 매핑
    @GetMapping("/my/review")
    public String review(){
        return "/my/review";
    }
}
