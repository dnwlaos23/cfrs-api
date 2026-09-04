package kr.or.kisa.cfrs.xrayServer.api.verify;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.model.Sorts;
import kr.or.kisa.cfrs.xrayServer.api.verify.dto.XraySharingDataCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class XrayVerifyDao {
    @Autowired
    @Qualifier("cfrsDatabase")
    private MongoDatabase cfrsDb;

    public XraySharingDataCache verify(String recvUrl) {
        MongoCollection<XraySharingDataCache> coll = cfrsDb.getCollection(
                XraySharingDataCache.COLL_NAME, XraySharingDataCache.class);
        return coll.find(eq("recvUrl", recvUrl))
                .sort(Sorts.descending("_id"))
                .first();
    }
}
