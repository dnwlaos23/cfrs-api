package kr.or.kisa.cfrs.xrayServer.api.share.dto;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = XraySharingData.COLL_NAME)
public class XraySharingData {
    public static final String COLL_NAME = "sharingData";

    @BsonId
    private ObjectId id;

    @BsonProperty("firstURL")
    private String firstURL;

    private String smStatus;

    public XraySharingData() {
    }

    // TODO: 추후 recvUrl로 변경 필요
    public XraySharingData(ObjectId id, String firstURL, String smStatus) {
        this.id = id;
        this.firstURL = firstURL;
        this.smStatus = smStatus;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getFirstURL() {
        return firstURL;
    }

    public void setFirstURL(String firstURL) {
        this.firstURL = firstURL;
    }

    public String getSmStatus() {
        return smStatus;
    }

    public void setSmStatus(String smStatus) {
        this.smStatus = smStatus;
    }
}
