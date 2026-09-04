package kr.or.kisa.cfrs.xrayServer.api.share;

import jakarta.servlet.http.HttpServletRequest;
import kr.or.kisa.cfrs.xrayServer.api.share.dto.XrayShareRequest;
import kr.or.kisa.cfrs.xrayServer.api.share.dto.XraySharingData;
import kr.or.kisa.cfrs.xrayServer.api.share.dto.XraySharingChannel;
import kr.or.kisa.cfrs.xrayServer.api.common.dto.XrayResponse;
import kr.or.kisa.cfrs.xrayServer.api.common.dto.ApiType;
import kr.or.kisa.cfrs.xrayServer.api.common.CommonApiManager;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class XrayShareService {
    @Value("${share.max-period-months:1}")
    private int maxPeriodMonths;

    @Autowired
    private XrayShareDao xrayShareDao;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private CommonApiManager apiManager;

    public XrayResponse share(XrayShareRequest request, HttpServletRequest httpRequest) throws Exception {
        String channelName = (String) httpRequest.getAttribute("channelName");
        Long stime = request.getStime();

        Bson filter = buildFilter(stime, channelName);

        List<XraySharingData> dataList = xrayShareDao.share(filter);

        // 3. 결과 변환 및 채널 정보의 lastData 업데이트
        List<XrayResponse.ResData> resDatas = convertAndSaveLastData(dataList, channelName);

        XrayResponse response = new XrayResponse(0, "요청이 정상적으로 접수되었습니다.", resDatas);

        // 4. 통계 및 로그 기록 (dataCnt 값에 실제 데이터 목록 개수 설정)
        apiManager.recordApiStats(channelName, ApiType.SHARE.name(), dataList.size());

        String reqJson = apiManager.convertToJson(request);
        String resJson = apiManager.convertToJson(response);
        apiManager.recordLog(channelName, ApiType.SHARE.name(), true, 200, reqJson, resJson, httpRequest);

        return response;
    }

    private List<XrayResponse.ResData> convertAndSaveLastData(List<XraySharingData> dataList, String channelName) {
        List<XrayResponse.ResData> resDatas = new ArrayList<>();
        for (XraySharingData data : dataList) {
            resDatas.add(new XrayResponse.ResData(data.getFirstURL(), data.getSmStatus()));
        }

        if (!dataList.isEmpty()) {
            ObjectId lastId = dataList.get(dataList.size() - 1).getId();
            channelService.updateLastData(channelName, lastId);
        }

        return resDatas;
    }

    private Bson buildFilter(Long stime, String channelName) {
        if (stime != null && stime != 0L) {
            Date date = new Date(stime);
            ObjectId startId = new ObjectId(date);
            return Filters.gte("_id", startId);
        }

        Date minDate = Date.from(ZonedDateTime.now().minusMonths(maxPeriodMonths).toInstant());
        ObjectId minId = new ObjectId(minDate);

        XraySharingChannel channel = channelService.getChannel(channelName);
        ObjectId lastData = (channel != null) ? channel.getLastData() : null;
        if (lastData != null && lastData.compareTo(minId) > 0) {
            return Filters.gt("_id", lastData);
        }

        return Filters.gte("_id", minId);
    }
}
