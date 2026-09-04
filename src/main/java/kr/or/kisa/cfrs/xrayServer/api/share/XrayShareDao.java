package kr.or.kisa.cfrs.xrayServer.api.share;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;
import kr.or.kisa.cfrs.xrayServer.api.share.dto.XraySharingData;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class XrayShareDao {
    @Autowired
    @Qualifier("cfrsDatabase")
    private MongoDatabase cfrsDb;

    public List<XraySharingData> share(Bson filter) {
        MongoCollection<XraySharingData> coll = cfrsDb.getCollection(
                XraySharingData.COLL_NAME, XraySharingData.class);

        List<XraySharingData> list = new ArrayList<>();
        coll.find(filter)
                .sort(Sorts.ascending("_id"))
                .limit(1000)
                .into(list);

        return list;
    }
}
