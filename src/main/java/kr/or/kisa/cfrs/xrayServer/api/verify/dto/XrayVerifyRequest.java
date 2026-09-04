package kr.or.kisa.cfrs.xrayServer.api.verify.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class XrayVerifyRequest {
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$")
    private String sendId;

    @NotBlank
    @Pattern(regexp = "^(http|https)://.*")
    private String recvUrl;

    @NotNull
    private Integer count;

    public XrayVerifyRequest() {
    }

    public XrayVerifyRequest(String sendId, String recvUrl, Integer count) {
        this.sendId = sendId;
        this.recvUrl = recvUrl;
        this.count = count;
    }

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
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
