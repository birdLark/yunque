package www.larkmidtable.com.channel;


import www.larkmidtable.com.queue.KafkaQueue;
import www.larkmidtable.com.transformer.TransformerExecution;

import java.util.List;
import java.util.Queue;

/**
 * @Description:  kafka通道
 * @Date: 2022/12/2 11:43
 */
public class KafkaChannel extends  Channel {


    public KafkaChannel(String host, String topic, String clientId, String groupId,List<TransformerExecution> transformerExecutionList ) {
        Queue<List<String>> kafkaQueue = new KafkaQueue(host,topic,clientId,groupId,transformerExecutionList);
        this.setQueue(kafkaQueue);
    }
}
