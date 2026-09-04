package kr.or.kisa.cfrs.xrayServer.api.common;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;

import kr.or.kisa.cfrs.xrayServer.api.common.dto.XrayLog;
import kr.or.kisa.cfrs.xrayServer.api.common.dto.XrayStat;

import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CommonApiDao {
    @Autowired
    private MongoDatabase mongo;

    /**
     * 채널별 api 일일 호출 통계 기록
     */
    public void recordApiStats(Bson filter, long dataCnt) {
        Bson update = Updates.combine(
                Updates.inc("callCnt", 1),
                Updates.inc("dataCnt", dataCnt));

        UpdateOptions options = new UpdateOptions().upsert(true);
        MongoCollection<XrayStat> coll = mongo.getCollection(XrayStat.COLL_NAME, XrayStat.class);
        coll.updateOne(filter, update, options);
    }

    /**
     * 채널별 api 호출 이력 생성
     */
    public void recordApiLog(XrayLog xrayLog) {
        MongoCollection<XrayLog> coll = mongo.getCollection(XrayLog.COLL_NAME, XrayLog.class);
        coll.insertOne(xrayLog);
    }
}
