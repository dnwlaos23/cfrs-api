package kr.or.kisa.cfrs.xrayServer.api.share.dto;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = XraySharingChannel.COLL_NAME)
public class XraySharingChannel {
    public static final String COLL_NAME = "sharingChannel";

    @BsonId
    private ObjectId id;
    private String name;
    private ObjectId lastData;

    public XraySharingChannel() {}

    public XraySharingChannel(ObjectId id, String name, ObjectId lastData) {
        this.id = id;
        this.name = name;
        this.lastData = lastData;
    }

    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public ObjectId getLastData() { return lastData; }
    public void setLastData(ObjectId lastData) { this.lastData = lastData; }
}
