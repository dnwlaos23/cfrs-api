package kr.or.kisa.cfrs.xrayServer.config;

import kr.or.kisa.cfrs.xrayServer.api.share.dto.XrayShareRequest;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SharePeriodConfig {

    @Value("${share.max-period-months}")
    private int maxPeriodMonths;

    @PostConstruct
    public void init() {
        XrayShareRequest.setMaxPeriodMonths(maxPeriodMonths);
    }
}
