package www.larkmidtable.com.channel;


import www.larkmidtable.com.queue.KafkaQueue;

import java.util.List;
import java.util.Queue;

/**
 * @Description:  kafka通道
 * @Date: 2022/12/2 11:43
 */
public class KafkaChannel extends  Channel {


    public KafkaChannel(String host, String topic, String clientId, String groupId) {
        Queue<List<String>> kafkaQueue = new KafkaQueue(host,topic,clientId,groupId);
        this.setQueue(kafkaQueue);
    }
}
