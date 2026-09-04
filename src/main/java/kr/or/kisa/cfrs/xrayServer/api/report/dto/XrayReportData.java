package kr.or.kisa.cfrs.xrayServer.api.report.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class XrayReportData {
    @NotBlank
    @Pattern(regexp = "^(http|https)://.*")
    private String recvUrl;

    @NotNull
    private Integer count;

    public XrayReportData() {
    }

    public XrayReportData(String recvUrl, Integer count) {
        this.recvUrl = recvUrl;
        this.count = count;
    }

    public String getRecvUrl() {
        return recvUrl;
    }

    public void setRecvUrl(String recvUrl) {
        this.recvUrl = recvUrl;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
