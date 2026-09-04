package kr.or.kisa.cfrs.xrayServer.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.util.ContentCachingRequestWrapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class RequestUtil {

    private RequestUtil() {
        // 인스턴스화 방지
    }

    /**
     * 로그 기록 등의 목적으로 Request Body를 여러 번 안전하게 읽을 수 있도록 캐싱하여 추출
     */
    public static String getRequestBody(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper) {
            ContentCachingRequestWrapper wrapper = (ContentCachingRequestWrapper) request;
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length == 0) {
                try {
                    wrapper.getInputStream().readAllBytes();
                    buf = wrapper.getContentAsByteArray();
                } catch (IOException ignored) {
                }
            }
            return new String(buf, StandardCharsets.UTF_8);
        }
        return "";
    }
}
