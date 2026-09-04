package kr.or.kisa.cfrs.xrayServer.api.report;

import kr.or.kisa.cfrs.xrayServer.api.common.dto.ApiType;
import kr.or.kisa.cfrs.xrayServer.api.report.dto.XrayReportData;
import kr.or.kisa.cfrs.xrayServer.api.report.dto.XrayReport;
import kr.or.kisa.cfrs.xrayServer.api.report.dto.XrayReportRequest;
import kr.or.kisa.cfrs.xrayServer.api.common.CommonApiManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Slf4j
@Service
public class XrayReportService {
    @Autowired
    private XrayReportDao reportDao;
    @Autowired
    private CommonApiManager apiManager;

    public void report(XrayReportRequest request, HttpServletRequest httpRequest)
            throws Exception {

        String channelName = (String) httpRequest.getAttribute("channelName");
        saveReport(request, channelName);

        // 통계 컬렉션 업데이트
        apiManager.recordApiStats(channelName, ApiType.REPORT.name(), 0);

        // 이력 기록
        String reqJson = apiManager.convertToJson(request);
        if (reqJson == null) {
            reqJson = "";
        }

        String resJson = "{\"resCode\":0,\"resMsg\":\"요청이 정상적으로 접수되었습니다.\"}";
        apiManager.recordLog(channelName, ApiType.REPORT.name(), true, 200, reqJson, resJson, httpRequest);
    }

    private void saveReport(XrayReportRequest request, String channelName) {
        if (request.getDatas() == null) {
            return;
        }
        for (XrayReportData data : request.getDatas()) {
            XrayReport report = new XrayReport(
                    channelName,
                    request.getSendId(),
                    request.getBlockId(),
                    data.getRecvUrl(),
                    data.getCount(),
                    LocalDateTime.now());
            reportDao.report(report);
        }
    }
}
