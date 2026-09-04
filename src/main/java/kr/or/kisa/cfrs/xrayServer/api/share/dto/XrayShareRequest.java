package kr.or.kisa.cfrs.xrayServer.api.share.dto;

import jakarta.validation.constraints.AssertTrue;
import java.time.ZonedDateTime;

public class XrayShareRequest {
    private static int maxPeriodMonths = 1;

    public static void setMaxPeriodMonths(int months) {
        maxPeriodMonths = months;
    }

    private Long stime;

    public XrayShareRequest() {
    }

    public XrayShareRequest(Long stime) {
        this.stime = stime;
    }

    public Long getStime() {
        return (stime != null && stime == 0L) ? null : stime;
    }

    public void setStime(Long stime) {
        this.stime = (stime != null && stime == 0L) ? null : stime;
    }

    @AssertTrue
    public boolean isValidStimeFormat() {
        if (stime == null || stime == 0L) {
            return true;
        }
        return String.valueOf(stime).length() == 13;
    }

    @AssertTrue
    public boolean isValidStimeNotFuture() {
        if (stime == null || stime == 0L) {
            return true;
        }
        return stime <= System.currentTimeMillis();
    }

    @AssertTrue
    public boolean isValidStimeRange() {
        if (stime == null || stime == 0L) {
            return true;
        }
        long maxAgoMs = ZonedDateTime.now().minusMonths(maxPeriodMonths).toInstant().toEpochMilli();
        return stime >= maxAgoMs;
    }
}
