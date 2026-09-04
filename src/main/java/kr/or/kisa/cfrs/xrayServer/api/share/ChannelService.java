package kr.or.kisa.cfrs.xrayServer.api.share;

import kr.or.kisa.cfrs.xrayServer.api.share.dto.XraySharingChannel;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChannelService {
    @Autowired
    private ChannelDao channelDao;

    public XraySharingChannel getChannel(String channelName) {
        return channelDao.getChannel(channelName);
    }

    public void updateLastData(String channelName, ObjectId lastData) {
        channelDao.updateLastData(channelName, lastData);
    }
}
