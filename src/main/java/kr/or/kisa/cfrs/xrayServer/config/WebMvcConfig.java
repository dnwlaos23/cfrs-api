package kr.or.kisa.cfrs.xrayServer.config;

import kr.or.kisa.cfrs.xrayServer.interceptor.XrayAPIInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final XrayAPIInterceptor xrayAPIInterceptor;

    public WebMvcConfig(XrayAPIInterceptor xrayAPIInterceptor) {
        this.xrayAPIInterceptor = xrayAPIInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(xrayAPIInterceptor)
                .addPathPatterns("/cfrs/api/xray/**");
    }
}
