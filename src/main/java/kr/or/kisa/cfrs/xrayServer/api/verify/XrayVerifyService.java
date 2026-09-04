package kr.or.kisa.cfrs.xrayServer.api.verify;

import kr.or.kisa.cfrs.xrayServer.api.common.dto.ApiType;
import kr.or.kisa.cfrs.xrayServer.api.common.dto.XrayResponse;
import kr.or.kisa.cfrs.xrayServer.api.common.CommonApiManager;
import kr.or.kisa.cfrs.xrayServer.api.verify.dto.XrayVerifyRequest;
import kr.or.kisa.cfrs.xrayServer.api.verify.dto.XraySharingDataCache;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class XrayVerifyService {
    @Autowired
    private XrayVerifyDao verifyDao;
    @Autowired
    private CommonApiManager apiManager;

    public XrayResponse verify(XrayVerifyRequest request, HttpServletRequest httpRequest) throws Exception {
        String channelName = (String) httpRequest.getAttribute("channelName");

        XraySharingDataCache cache = verifyDao.verify(request.getRecvUrl());

        String smStatus = "미확인";
        if (cache != null && cache.getSmStatus() != null) {
            smStatus = cache.getSmStatus();
        }

        List<XrayResponse.ResData> resDatas = new ArrayList<>();
        resDatas.add(new XrayResponse.ResData(request.getRecvUrl(), smStatus));

        XrayResponse response = new XrayResponse(0, "요청이 정상적으로 접수되었습니다.", resDatas);

        apiManager.recordApiStats(channelName, ApiType.VERIFY.name(), 0);

        String reqJson = apiManager.convertToJson(request);
        String resJson = apiManager.convertToJson(response);
        apiManager.recordLog(channelName, ApiType.VERIFY.name(), true, 200, reqJson, resJson, httpRequest);

        return response;
    }
}
