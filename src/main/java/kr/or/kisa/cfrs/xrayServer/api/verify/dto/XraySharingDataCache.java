package kr.or.kisa.cfrs.xrayServer.api.verify.dto;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = XraySharingDataCache.COLL_NAME)
public class XraySharingDataCache {
    public static final String COLL_NAME = "sharingData.cache";

    @BsonId
    private ObjectId id;
    private String recvUrl;
    private String recvHash;
    private String smStatus;
    private LocalDateTime recvDate;
    private LocalDateTime createDate;

    public XraySharingDataCache() {
    }

    public XraySharingDataCache(ObjectId id, String recvUrl, String recvHash, String smStatus, LocalDateTime recvDate, LocalDateTime createDate) {
        this.id = id;
        this.recvUrl = recvUrl;
        this.recvHash = recvHash;
        this.smStatus = smStatus;
        this.recvDate = recvDate;
        this.createDate = createDate;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getRecvUrl() {
        return recvUrl;
    }

    public void setRecvUrl(String recvUrl) {
        this.recvUrl = recvUrl;
    }

    public String getRecvHash() {
        return recvHash;
    }

    public void setRecvHash(String recvHash) {
        this.recvHash = recvHash;
    }

    public String getSmStatus() {
        return smStatus;
    }

    public void setSmStatus(String smStatus) {
        this.smStatus = smStatus;
    }

    public LocalDateTime getRecvDate() {
        return recvDate;
    }

    public void setRecvDate(LocalDateTime recvDate) {
        this.recvDate = recvDate;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }
}
