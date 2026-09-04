package kr.or.kisa.cfrs.xrayServer.api.common.dto;

import java.util.List;

public class XrayResponse {
    private int resCode;
    private String resMsg;
    private List<ResData> resDatas;

    public XrayResponse() {
    }

    public XrayResponse(int resCode, String resMsg) {
        this.resCode = resCode;
        this.resMsg = resMsg;
    }

    public XrayResponse(int resCode, String resMsg, List<ResData> resDatas) {
        this.resCode = resCode;
        this.resMsg = resMsg;
        this.resDatas = resDatas;
    }

    public int getResCode() {
        return resCode;
    }

    public void setResCode(int resCode) {
        this.resCode = resCode;
    }

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

    public List<ResData> getResDatas() {
        return resDatas;
    }

    public void setResDatas(List<ResData> resDatas) {
        this.resDatas = resDatas;
    }

    public static class ResData {
        private String recvUrl;
        private String smStatus;

        public ResData() {
        }

        public ResData(String recvUrl, String smStatus) {
            this.recvUrl = recvUrl;
            this.smStatus = smStatus;
        }

        public String getRecvUrl() {
            return recvUrl;
        }

        public void setRecvUrl(String recvUrl) {
            this.recvUrl = recvUrl;
        }

        public String getSmStatus() {
            return smStatus;
        }

        public void setSmStatus(String smStatus) {
            this.smStatus = smStatus;
        }
    }
}
