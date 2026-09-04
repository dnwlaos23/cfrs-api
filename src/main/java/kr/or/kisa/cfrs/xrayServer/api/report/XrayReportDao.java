package kr.or.kisa.cfrs.xrayServer.api.report;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import kr.or.kisa.cfrs.xrayServer.api.report.dto.XrayReport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class XrayReportDao {
    @Autowired
    private MongoDatabase mongo;

    public void report(XrayReport report) {
        MongoCollection<XrayReport> coll = mongo.getCollection(XrayReport.COLL_NAME, XrayReport.class);
        coll.insertOne(report);
    }
}
