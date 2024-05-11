package kr.co.lotteon.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.lotteon.dto.admin.BannerDTO;
import kr.co.lotteon.service.admin.BannerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class MyBannerInterceptor implements HandlerInterceptor {

    private final BannerService bannerService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if(modelAndView != null){
            List<BannerDTO> banners = bannerService.selectBanners("myPage");
            log.info("!!! : "+ banners);
            modelAndView.addObject("banners", banners);
        }
    }
}
