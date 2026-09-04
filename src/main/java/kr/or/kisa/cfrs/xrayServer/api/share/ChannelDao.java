package kr.or.kisa.cfrs.xrayServer.api.share;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import kr.or.kisa.cfrs.xrayServer.api.share.dto.XraySharingChannel;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import static com.mongodb.client.model.Filters.eq;

@Repository
public class ChannelDao {
    @Autowired
    @Qualifier("cfrsDatabase")
    private MongoDatabase cfrsDb;

    public XraySharingChannel getChannel(String channelName) {
        MongoCollection<XraySharingChannel> coll = cfrsDb.getCollection(
                XraySharingChannel.COLL_NAME, XraySharingChannel.class);
        return coll.find(eq("name", channelName)).first();
    }

    public void updateLastData(String channelName, ObjectId lastData) {
        MongoCollection<XraySharingChannel> coll = cfrsDb.getCollection(
                XraySharingChannel.COLL_NAME, XraySharingChannel.class);
        UpdateOptions options = new UpdateOptions().upsert(true);
        coll.updateOne(eq("name", channelName), Updates.set("lastData", lastData), options);
    }
}
