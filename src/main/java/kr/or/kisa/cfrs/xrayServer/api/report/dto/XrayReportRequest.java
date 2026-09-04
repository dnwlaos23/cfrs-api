package kr.or.kisa.cfrs.xrayServer.api.report.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import java.util.List;

public class XrayReportRequest {
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$")
    private String sendId;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$")
    private String blockId;

    @NotEmpty
    @Valid
    private List<XrayReportData> datas;

    public XrayReportRequest() {
    }

    public XrayReportRequest(String sendId, String blockId, List<XrayReportData> datas) {
        this.sendId = sendId;
        this.blockId = blockId;
        this.datas = datas;
    }

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public List<XrayReportData> getDatas() {
        return datas;
    }

    public void setDatas(List<XrayReportData> datas) {
        this.datas = datas;
    }
}
