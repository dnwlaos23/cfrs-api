package kr.or.kisa.cfrs.xrayServer.handler;

import kr.or.kisa.cfrs.xrayServer.api.common.CommonApiManager;
import kr.or.kisa.cfrs.xrayServer.api.common.dto.XrayResponse;
import kr.or.kisa.cfrs.xrayServer.util.JwtUtil;
import kr.or.kisa.cfrs.xrayServer.util.RequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private CommonApiManager apiManager;
    @Autowired
    private JwtUtil jwtUtil;

    @Value("${share.max-period-months:1}")
    private int maxPeriodMonths;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String fieldName = fieldError != null ? fieldError.getField() : "unknown";
        String code = fieldError != null ? fieldError.getCode() : "";

        boolean isMissingRequired = "NotBlank".equals(code) || "NotNull".equals(code) || "NotEmpty".equals(code);

        XrayResponse validationError = switch (fieldName) {
            case "sendId" -> isMissingRequired
                    ? new XrayResponse(10, "필수 입력 항목인 'sendId' 필드가 누락되었거나 비어 있습니다.")
                    : new XrayResponse(11, "입력 필드 'sendId'의 데이터 포맷이 규격에 맞지 않습니다.");
            case "blockId", "recvUrl" -> isMissingRequired
                    ? new XrayResponse(20, "필수 입력 항목인 '" + fieldName + "' 필드가 누락되었거나 비어 있습니다.")
                    : new XrayResponse(21, "입력 필드 '" + fieldName + "'의 데이터 포맷이 규격에 맞지 않습니다.");
            case "count" -> isMissingRequired
                    ? new XrayResponse(30, "필수 입력 항목인 'count' 필드가 누락되었거나 비어 있습니다.")
                    : new XrayResponse(31, "입력 필드 'count'의 데이터 포맷이 규격에 맞지 않습니다.");
            case String f when f.startsWith("datas") -> ("datas".equals(f) && isMissingRequired)
                    ? new XrayResponse(30, "필수 입력 항목인 'datas' 필드가 누락되었거나 비어 있습니다.")
                    : new XrayResponse(31, "입력 필드 'datas'의 데이터 포맷이 규격에 맞지 않습니다.");
            case "validStimeFormat" -> new XrayResponse(11, "날짜 데이터 ‘stime’의 데이터 포맷이 규격에 맞지 않습니다.");
            case "validStimeNotFuture" -> new XrayResponse(12, "날짜 데이터 ‘stime’의 시간이 현재 시간 이후입니다.");
            case "validStimeRange" -> new XrayResponse(10, "최대 조회 기간은 " + maxPeriodMonths + "개월입니다.");
            default -> new XrayResponse(400, "입력 데이터 검증에 실패했습니다.");
        };

        logError(request, HttpStatus.BAD_REQUEST.value(), validationError.getResCode(), validationError.getResMsg());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationError);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            HttpServletRequest request) throws HttpMessageNotReadableException {

        boolean isDatasError = false;

        // 1. Check message content
        String msg = ex.getMessage();
        if (msg != null && (msg.contains("datas") || msg.contains("XrayReportData") || msg.contains("ArrayList"))) {
            isDatasError = true;
        }

        // 2. Check Jackson path references
        Throwable cause = ex.getCause();
        if (!isDatasError && cause instanceof JsonMappingException jme) {
            var path = jme.getPath();
            if (path != null) {
                for (var ref : path) {
                    if ("datas".equals(ref.getFieldName())) {
                        isDatasError = true;
                        break;
                    }
                }
            }
        }

        if (isDatasError) {
            XrayResponse validationError = new XrayResponse(31, "입력 필드 'datas'의 데이터 포맷이 규격에 맞지 않습니다.");
            logError(request, HttpStatus.BAD_REQUEST.value(), validationError.getResCode(),
                    validationError.getResMsg());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationError);
        }

        throw ex;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> handleNoHandlerFound(NoHandlerFoundException ex, HttpServletRequest request) {
        logError(request, HttpStatus.NOT_FOUND.value(), 404, "잘못된 URL 접근");
        XrayResponse body = new XrayResponse(404, "잘못된 URL 접근");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex,
            HttpServletRequest request) {
        logError(request, HttpStatus.METHOD_NOT_ALLOWED.value(), 405, "잘못된 HTTP 메서드 요청");
        XrayResponse body = new XrayResponse(405, "잘못된 HTTP 메서드 요청");
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(Exception ex, HttpServletRequest request) {
        log.error("Unhandled Exception: ", ex);
        logError(request, HttpStatus.INTERNAL_SERVER_ERROR.value(), 999, "알 수 없는 오류가 발생했습니다.");
        XrayResponse body = new XrayResponse(999, "알 수 없는 오류가 발생했습니다.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body);
    }

    private void logError(HttpServletRequest request, int httpStatus, int resCode, String resMsg) {
        String channelName = "UNKNOWN";

        String token = jwtUtil.resolveToken(request);
        if (token != null) {
            try {
                channelName = jwtUtil.getChannelName(token);
            } catch (Exception e) {
                log.error(e.toString(), e);
            }
        }

        String apiType = request.getAttribute("apiType") != null
                ? (String) request.getAttribute("apiType")
                : "UNKNOWN";

        String reqJson = RequestUtil.getRequestBody(request);
        if (reqJson == null || reqJson.trim().isEmpty()) {
            reqJson = "{}";
        }
        String resJson = String.format("{\"resCode\":%d,\"resMsg\":\"%s\"}", resCode, resMsg);

        log.debug("[{}] failed ({}, {})\n  Request : {}\n  Response: {}",
                request.getRequestURI(), resCode, channelName, reqJson, resJson);

        apiManager.recordLog(channelName, apiType, false, httpStatus, reqJson, resJson, request);
    }
}
