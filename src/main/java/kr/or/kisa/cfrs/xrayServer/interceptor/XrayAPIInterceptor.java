package kr.or.kisa.cfrs.xrayServer.interceptor;

import kr.or.kisa.cfrs.xrayServer.auth.ApiAccessControl;
import kr.or.kisa.cfrs.xrayServer.api.common.CommonApiManager;
import kr.or.kisa.cfrs.xrayServer.api.common.dto.XrayLog;
import kr.or.kisa.cfrs.xrayServer.util.JwtUtil;
import kr.or.kisa.cfrs.xrayServer.util.RequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class XrayAPIInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CommonApiManager apiManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        ApiAccessControl permission = handlerMethod.getMethodAnnotation(ApiAccessControl.class);
        if (permission == null) {
            return true;
        }

        // 요청 시작 시간 기록
        request.setAttribute("requestStartTime", LocalDateTime.now());

        // 1. 헤더 내부 토큰 유효성 검증
        String token = jwtUtil.resolveToken(request);

        if (token == null) {
            sendErrorResponse(request, response, HttpServletResponse.SC_UNAUTHORIZED,
                    "토큰 인증정보가 없거나 유효하지 않습니다.");
            return false;
        }

        if (!jwtUtil.validateToken(token)) {
            sendErrorResponse(request, response, HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰입니다.");
            return false;
        }

        // Token 정보 추출
        String channelName = jwtUtil.getChannelName(token);
        List<String> allowedApis = jwtUtil.getAllowedApis(token);
        request.setAttribute("channelName", channelName);
        request.setAttribute("apiType", permission.value().name());

        // 2. URL 접근 권한 확인
        String requiredClaim = permission.value().name(); // 예: "SHARE", "REPORT", "VERIFY"
        if (!allowedApis.contains(requiredClaim)) {
            sendErrorResponse(request, response, HttpServletResponse.SC_FORBIDDEN, "접근이 불가한 API 입니다.");
            return false;
        }

        return true;
    }

    private void sendErrorResponse(HttpServletRequest request, HttpServletResponse response, int status, String message)
            throws Exception {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        String errorResponseJson = String.format("{\"resCode\":%d,\"resMsg\":\"%s\"}", status, message);
        response.getWriter().write(errorResponseJson);

        String channelName = request.getAttribute("channelName") != null ? (String) request.getAttribute("channelName")
                : "UNKNOWN";
        String apiType = request.getAttribute("apiType") != null ? (String) request.getAttribute("apiType") : "UNKNOWN";
        LocalDateTime requestStartTime = request.getAttribute("requestStartTime") != null
                ? (LocalDateTime) request.getAttribute("requestStartTime")
                : LocalDateTime.now();

        String reqJson = RequestUtil.getRequestBody(request);

        XrayLog log = XrayLog.builder()
                .channelName(channelName)
                .apiType(apiType)
                .callSuccess(false)
                .httpResCode(status)
                .reqJson(reqJson)
                .resJson(errorResponseJson)
                .reqDate(requestStartTime)
                .resDate(LocalDateTime.now())
                .build();

        apiManager.recordApiLog(log);
    }
}
