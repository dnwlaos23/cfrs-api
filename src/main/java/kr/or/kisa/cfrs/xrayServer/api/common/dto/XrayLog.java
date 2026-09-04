package kr.or.kisa.cfrs.xrayServer.api.common.dto;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = XrayLog.COLL_NAME)
public class XrayLog {
    public static final String COLL_NAME = "xray.api.log";

    @BsonId
    private ObjectId id;
    private String channelName;
    private String apiType;
    private boolean callSuccess;
    private int httpResCode;
    private String reqJson;
    private String resJson;
    private LocalDateTime reqDate;
    private LocalDateTime resDate;

    public XrayLog() {
    }

    public XrayLog(ObjectId id, String channelName, String apiType, boolean callSuccess, int httpResCode,
            String reqJson, String resJson, LocalDateTime reqDate, LocalDateTime resDate) {
        this.id = id;
        this.channelName = channelName;
        this.apiType = apiType;
        this.callSuccess = callSuccess;
        this.httpResCode = httpResCode;
        this.reqJson = reqJson;
        this.resJson = resJson;
        this.reqDate = reqDate;
        this.resDate = resDate;
    }

    public ObjectId getId() {
        return id;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getApiType() {
        return apiType;
    }

    public boolean isCallSuccess() {
        return callSuccess;
    }

    public int getHttpResCode() {
        return httpResCode;
    }

    public String getReqJson() {
        return reqJson;
    }

    public String getResJson() {
        return resJson;
    }

    public LocalDateTime getReqDate() {
        return reqDate;
    }

    public LocalDateTime getResDate() {
        return resDate;
    }

    public static XrayLogBuilder builder() {
        return new XrayLogBuilder();
    }

    public static class XrayLogBuilder {
        private ObjectId id;
        private String channelName;
        private String apiType;
        private boolean callSuccess;
        private int httpResCode;
        private String reqJson;
        private String resJson;
        private LocalDateTime reqDate;
        private LocalDateTime resDate;

        public XrayLogBuilder id(ObjectId id) {
            this.id = id;
            return this;
        }

        public XrayLogBuilder channelName(String channelName) {
            this.channelName = channelName;
            return this;
        }

        public XrayLogBuilder apiType(String apiType) {
            this.apiType = apiType;
            return this;
        }

        public XrayLogBuilder callSuccess(boolean callSuccess) {
            this.callSuccess = callSuccess;
            return this;
        }

        public XrayLogBuilder httpResCode(int httpResCode) {
            this.httpResCode = httpResCode;
            return this;
        }

        public XrayLogBuilder reqJson(String reqJson) {
            this.reqJson = reqJson;
            return this;
        }

        public XrayLogBuilder resJson(String resJson) {
            this.resJson = resJson;
            return this;
        }

        public XrayLogBuilder reqDate(LocalDateTime reqDate) {
            this.reqDate = reqDate;
            return this;
        }

        public XrayLogBuilder resDate(LocalDateTime resDate) {
            this.resDate = resDate;
            return this;
        }

        public XrayLog build() {
            return new XrayLog(id, channelName, apiType, callSuccess, httpResCode, reqJson, resJson, reqDate, resDate);
        }
    }
}
