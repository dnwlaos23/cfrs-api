package kr.or.kisa.cfrs.xrayServer.api.common.dto;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = XrayStat.COLL_NAME)
@CompoundIndex(name = "idx_channel_date_type", def = "{'channelName': 1, 'date': 1, 'apiType': 1}", unique = true)
public class XrayStat {
    public static final String COLL_NAME = "xray.api.stat";

    @BsonId
    private ObjectId id;
    private String channelName;
    private String date; // yyyy.mm.dd
    private String apiType; // SHARE, REPORT, VERIFY
    private long callCnt;
    private long dataCnt;

    public XrayStat() {
    }

    public XrayStat(ObjectId id, String channelName, String date, String apiType, long callCnt,
            long dataCnt) {
        this.id = id;
        this.channelName = channelName;
        this.date = date;
        this.apiType = apiType;
        this.callCnt = callCnt;
        this.dataCnt = dataCnt;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getApiType() {
        return apiType;
    }

    public void setApiType(String apiType) {
        this.apiType = apiType;
    }

    public long getCallCnt() {
        return callCnt;
    }

    public void setCallCnt(long callCnt) {
        this.callCnt = callCnt;
    }

    public long getDataCnt() {
        return dataCnt;
    }

    public void setDataCnt(long dataCnt) {
        this.dataCnt = dataCnt;
    }
}
