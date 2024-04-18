package kr.co.lotteon.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;

public class LocalPathInterceptor implements HandlerInterceptor {

    @Value("${local.static-resources-path} " + "/**")
    private String localPath;

    public LocalPathInterceptor(String localPath) {
        this.localPath = localPath;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("localPath: " + localPath);
        return true;
    }
}
