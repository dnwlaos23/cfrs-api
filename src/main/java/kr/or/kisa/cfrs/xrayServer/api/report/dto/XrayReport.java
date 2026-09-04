package kr.or.kisa.cfrs.xrayServer.api.report.dto;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = XrayReport.COLL_NAME)
public class XrayReport {
    public static final String COLL_NAME = "xray.report";

    @BsonId
    private ObjectId id;
    private String channelName;
    private String sendId;
    private String blockId;
    private String recvUrl;
    private Integer count;
    private LocalDateTime reportedDate;

    public XrayReport() {
    }

    public XrayReport(String channelName, String sendId, String blockId, String recvUrl, Integer count,
            LocalDateTime reportedDate) {
        this.channelName = channelName;
        this.sendId = sendId;
        this.blockId = blockId;
        this.recvUrl = recvUrl;
        this.count = count;
        this.reportedDate = reportedDate;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
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

    public LocalDateTime getReportedDate() {
        return reportedDate;
    }

    public void setReportedDate(LocalDateTime reportedDate) {
        this.reportedDate = reportedDate;
    }
}
