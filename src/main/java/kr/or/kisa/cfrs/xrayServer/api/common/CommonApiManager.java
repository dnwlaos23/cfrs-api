package kr.or.kisa.cfrs.xrayServer.api.common;

import kr.or.kisa.cfrs.xrayServer.api.common.dto.XrayLog;

import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class CommonApiManager {
    @Autowired
    private CommonApiDao commonApiDao;
    @Autowired
    private ObjectMapper objectMapper;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    public void recordApiStats(String channelName, String apiType, long dataCnt) {
        Bson filter = createDailyStatsFilter(channelName, apiType);
        commonApiDao.recordApiStats(filter, dataCnt);
    }

    public void recordApiLog(XrayLog xrayLog) {
        commonApiDao.recordApiLog(xrayLog);
    }


    private Bson createDailyStatsFilter(String channelName, String apiType) {
        String today = LocalDate.now().format(DATE_FORMATTER);
        return Filters.and(
                Filters.eq("channelName", channelName),
                Filters.eq("date", today),
                Filters.eq("apiType", apiType));
    }

    public String convertToJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("Failed to convert object to JSON string", e);
            return "";
        }
    }

    public void recordLog(String channelName, String apiType, boolean callSuccess, int httpResCode,
                          String reqJson, String resJson, HttpServletRequest httpRequest) {
        LocalDateTime requestStartTime = (LocalDateTime) httpRequest.getAttribute("requestStartTime");
        if (requestStartTime == null) {
            requestStartTime = LocalDateTime.now();
        }

        XrayLog xrayLog = XrayLog.builder()
                .channelName(channelName)
                .apiType(apiType)
                .callSuccess(callSuccess)
                .httpResCode(httpResCode)
                .reqJson(reqJson)
                .resJson(resJson)
                .reqDate(requestStartTime)
                .resDate(LocalDateTime.now())
                .build();

        recordApiLog(xrayLog);
    }
}
