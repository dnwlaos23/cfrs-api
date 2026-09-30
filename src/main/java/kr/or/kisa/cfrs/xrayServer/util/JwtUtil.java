package kr.or.kisa.cfrs.xrayServer.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtil {

    private final SecretKey secretKey;

    public JwtUtil(@Value("${jwt.secret}") String secretString) {

        byte[] keyBytes = Base64.getDecoder().decode(secretString.trim());
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * JWT 토큰 유효성 검증
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            log.error("[JwtUtil] Token validation failed - Reason: {} ({})", e.getMessage(), e.getClass().getSimpleName());
            return false;
        }
    }

    /**
     * 토큰에서 Claims 추출
     */
    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 토큰에서 채널명 추출
     */
    public String getChannelName(String token) {
        try {
            Claims claims = getClaims(token);
            String channelName = claims.get("channelName", String.class);
            return channelName != null ? channelName : "UNKNOWN";
        } catch (Exception e) {
            return "UNKNOWN";
        }
    }

    /**
     * 토큰에서 허용된 API 목록 추출 (RMUS 토큰의 callApiList 클레임 반환)
     */
    @SuppressWarnings("unchecked")
    public List<String> getAllowedApis(String token) {
        try {
            Claims claims = getClaims(token);
            List<String> allowedApis = claims.get("callApiList", List.class);
            if (allowedApis == null) {
                return Collections.emptyList();
            }
            return allowedApis;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    /**
     * 요청 헤더(Authorization 또는 Access-Token)에서 토큰 추출
     */
    public String resolveToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String accessTokenHeader = request.getHeader("Access-Token");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        } else if (accessTokenHeader != null) {
            return accessTokenHeader.startsWith("Bearer ") ? accessTokenHeader.substring(7) : accessTokenHeader;
        }
        return null;
    }
}
