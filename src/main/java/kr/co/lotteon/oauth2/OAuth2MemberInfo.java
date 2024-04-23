package kr.co.lotteon.oauth2;

import jakarta.servlet.http.HttpServletRequest;

public interface OAuth2MemberInfo {

    String getProviderId(); //공급자 아이디 ex) google, facebook
    String getProvider(); //공급자 ex) google, facebook
    String getName(); //사용자 이름 ex) 홍길동
    String getEmail();

    String getRegip(HttpServletRequest request);
}
