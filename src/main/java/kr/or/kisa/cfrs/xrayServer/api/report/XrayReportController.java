package kr.or.kisa.cfrs.xrayServer.api.report;

import kr.or.kisa.cfrs.xrayServer.auth.ApiAccessControl;
import kr.or.kisa.cfrs.xrayServer.api.common.dto.ApiType;
import kr.or.kisa.cfrs.xrayServer.api.report.dto.XrayReportRequest;
import kr.or.kisa.cfrs.xrayServer.api.common.dto.XrayResponse;
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
public class XrayReportController {
    @Autowired
    private XrayReportService reportService;

    @PostMapping("/report/smishing")
    @ApiAccessControl(ApiType.REPORT)
    public ResponseEntity<XrayResponse> reportSmishing(
            HttpServletRequest request,
            @Valid @RequestBody XrayReportRequest res) {
        XrayResponse response = new XrayResponse();

        try {
            reportService.report(res, request);

            response.setResCode(0);
            response.setResMsg("요청이 정상적으로 접수되었습니다.");

            log.debug("[/report/smishing] completed ({}, {})",
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
