package kr.or.kisa.cfrs.xrayServer.api.verify;

import kr.or.kisa.cfrs.xrayServer.auth.ApiAccessControl;
import kr.or.kisa.cfrs.xrayServer.api.common.dto.ApiType;
import kr.or.kisa.cfrs.xrayServer.api.common.dto.XrayResponse;
import kr.or.kisa.cfrs.xrayServer.api.verify.dto.XrayVerifyRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/cfrs/api/xray")
public class XrayVerifyController {
    @Autowired
    private XrayVerifyService verifyService;

    @PostMapping("/verify/smishing")
    @ApiAccessControl(ApiType.VERIFY)
    public ResponseEntity<XrayResponse> verifySmishing(
            HttpServletRequest request,
            @Valid @RequestBody XrayVerifyRequest res) {

        XrayResponse response = new XrayResponse();
        try {
            response = verifyService.verify(res, request);

            response.setResCode(0);
            response.setResMsg("요청이 정상적으로 접수되었습니다.");

            log.info("[/verify/smishing] completed ({}, {})",
                    response.getResCode(), request.getAttribute("channelName"));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error(e.toString(), e);

            response.setResCode(500);
            response.setResMsg("서버 오류가 발생했습니다.");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
