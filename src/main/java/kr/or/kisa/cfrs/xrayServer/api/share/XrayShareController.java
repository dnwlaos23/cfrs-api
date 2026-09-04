package kr.or.kisa.cfrs.xrayServer.api.share;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kr.or.kisa.cfrs.xrayServer.auth.ApiAccessControl;
import kr.or.kisa.cfrs.xrayServer.api.common.dto.ApiType;
import kr.or.kisa.cfrs.xrayServer.api.common.dto.XrayResponse;
import kr.or.kisa.cfrs.xrayServer.api.share.dto.XrayShareRequest;
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
public class XrayShareController {
    @Autowired
    private XrayShareService shareService;

    @PostMapping("/share/smishing")
    @ApiAccessControl(ApiType.SHARE)
    public ResponseEntity<XrayResponse> shareSmishing(
            HttpServletRequest request,
            @Valid @RequestBody XrayShareRequest res) {

        try {
            XrayResponse response = shareService.share(res, request);

            int count = (response.getResDatas() != null) ? response.getResDatas().size() : 0;

            log.debug("[/share/smishing] completed ({}, {}), Count : {}",
                    response.getResCode(), request.getAttribute("channelName"), count);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error(e.toString(), e);

            XrayResponse response = new XrayResponse(500, "서버 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
