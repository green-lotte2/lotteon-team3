package kr.co.lotteon.oauth2;


import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public class GoogleInfo implements OAuth2MemberInfo {
    public GoogleInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    private final Map<String, Object> attributes;
    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
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
