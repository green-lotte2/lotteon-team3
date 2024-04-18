package kr.co.lotteon.config;

import kr.co.lotteon.interceptor.AppInfoInterceptor;
import kr.co.lotteon.interceptor.LocalPathInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private AppInfo appInfo;
    
    // ====== 배포시엔 해당 어노테이션 사용 ======
//    @Value("${myServer.static-resources-path}")
    // 경로 :/home/lotteon/prodImg
    
    // ====== 개발시엔 해당 어노테이션 사용 ======
    @Value("${local.static-resources-path}")
    // 경로 :prod/**
    private String staticServerPath;

    @Value("${local.static-resources-path}")
    private String localPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("prodImg/**")
                .addResourceLocations("file:" + staticServerPath);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AppInfoInterceptor(appInfo));
        registry.addInterceptor(new LocalPathInterceptor(localPath));
    }

}
