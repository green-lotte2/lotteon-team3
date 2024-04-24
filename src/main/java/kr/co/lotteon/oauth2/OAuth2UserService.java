package kr.co.lotteon.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.lotteon.entity.member.Member;
import kr.co.lotteon.repository.member.MemberRepository;
import kr.co.lotteon.security.MyUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final HttpServletRequest request;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        String accessToken = userRequest.getAccessToken().getTokenValue();
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2User oauth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oauth2User.getAttributes();
        OAuth2MemberInfo memberInfo = null;

        if (registrationId.equals("google")) {
            memberInfo = new GoogleInfo(oauth2User.getAttributes());
        } else if (registrationId.equals("kakao")) {
            memberInfo = new KakaoInfo(oauth2User.getAttributes());
        }else if(registrationId.equals("naver")){
            //네이버는 (Map)으로 캐스팅을 해주어 getAttributes().get("response")를 해주어야 NaverUserInfo의 attributes에 값이 전달
            memberInfo=new NaverInfo((Map)oauth2User.getAttributes().get("response"));
        }



        // 사용자 확인 및 회원가입 처리
        String email = memberInfo.getEmail();
        String uid = email.substring(0, email.lastIndexOf("@"));
        String name = memberInfo.getName();
        String provider = memberInfo.getProvider();


        int level=1;
        String regip=memberInfo.getRegip(request);
        
        // 소셜 로그인시 이메일 중복되는 오류 수정
        Optional<Member> findMember = memberRepository.findByEmail(email);
        Member member=null;

        if(findMember.isEmpty()){
            member=Member.builder()
                    .uid(uid)
                    .email(email)
                    .name(name)
                    .nick(name)
                    .level(level)
                    .provider(provider)
                    .regip(regip)
                    .build();

            memberRepository.save(member);
        }else{
            member=findMember.get();
        }




        return MyUserDetails.builder()
                .member(member)
                .build();
    }
}
