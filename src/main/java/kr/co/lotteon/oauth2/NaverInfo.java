package kr.co.lotteon.oauth2;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public class NaverInfo implements OAuth2MemberInfo{
    private final Map<String, Object> attributes;

    public NaverInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getName() {
        return (String) attributes.get("nickname");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getRegip(HttpServletRequest request) {
        return request.getRemoteAddr();
    }
}
