package kr.or.kisa.cfrs.xrayServer.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import java.io.IOException;

/**
 * 에러 로그 등에서 API 요청 본문(JSON)을 여러 번 읽을 수 있도록
 * HttpServletRequest를 캐싱 래퍼로 감싸주는 필터
 */
@Component
public class ContentCachingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            chain.doFilter(new ContentCachingRequestWrapper((HttpServletRequest) request, 1024 * 1024), response);
        } else {
            chain.doFilter(request, response);
        }
    }
}
