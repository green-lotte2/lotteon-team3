package kr.co.lotteon.oauth2;


import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

import java.util.Map;

public class KakaoInfo implements OAuth2MemberInfo {
    private final Map<String, Object> attributes;
    private final Map<String, Object> kakaoAccountAttributes;
    private final Map<String, Object> profileAttributes;

    public KakaoInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
        this.kakaoAccountAttributes = (Map<String, Object>) attributes.get("kakao_account");
        this.profileAttributes = (Map<String, Object>) kakaoAccountAttributes.get("profile");

    }


    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getName() {
        return profileAttributes.get("nickname").toString();
    }

    @Override
    public String getEmail() {
        return kakaoAccountAttributes.get("email").toString();
    }

    @Override
    public String getRegip(HttpServletRequest request) {
        return request.getRemoteAddr();
    }

}