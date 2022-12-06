package www.larkmidtable.com.queue;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import www.larkmidtable.com.mq.ListDeserializer;
import www.larkmidtable.com.mq.ListSerializer;
import www.larkmidtable.com.transformer.TransformerExecution;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @ClassName: KafkaQueue
 * @Description:
 * @Author: Administrator
 * @Date: 2022/12/2 11:52
 */
public class KafkaQueue extends LinkedBlockingQueue {
    private static Logger logger = LoggerFactory.getLogger(KafkaQueue.class);
    private   String host = "hu_topic";
    private   String topic = "hu_topic";
    private   String clientId = "product.client.id.hu";
    private   String groupId = "group.hu";
    private static KafkaProducer<String,List<String>> producer = null;
    private static KafkaConsumer<String,List<String>> consumer = null;
    private static List<TransformerExecution> transformerExecutionList = null;





    public KafkaQueue(String host, String topic, String clientId, String groupId,List<TransformerExecution> transformerExecutionList) {
        this.host=host;
        this.topic=topic;
        this.clientId=clientId;
        this.groupId=groupId;
        this.transformerExecutionList=transformerExecutionList;
        init();
    }

    public void init(){
        Properties kafkaProducerConfig = getKafkaProducerConfig();
        producer = new KafkaProducer(kafkaProducerConfig);
        Properties kafkaConsumerConfig = getKafkaConsumerConfig();
        consumer = new KafkaConsumer(kafkaConsumerConfig);
        logger.debug("kafka 队列初始化完成");
    }

    private  Properties getKafkaProducerConfig(){
        Properties properties = new Properties();
        //根据发送消息的key类型来设置
        properties.put("bootstrap.servers", host);
        properties.put("acks", "all");
        properties.put("retries", 0);
        properties.put("batch.size", 16384);
        properties.put("key.serializer", StringSerializer.class.getName());
        properties.put("value.serializer", ListSerializer.class.getName());
        return properties;
    }

    private   Properties getKafkaConsumerConfig(){
        Properties properties = new Properties();
        //消息key解析 类
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        //消息value解析 类
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ListDeserializer.class.getName());
        //代理服务器地址列表(Broker List)
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,host);
        properties.put(ConsumerConfig.CLIENT_ID_CONFIG, clientId);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        //消费者组
        properties.put(ConsumerConfig.GROUP_ID_CONFIG,groupId);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);//
        return properties;
    }



    @Override
    public boolean add(Object o) {
        //消息实体
        ProducerRecord<String , List<String>> record = null;
            record = new ProducerRecord(topic, ( List<String>)o);
            //发送消息
            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if (null != e){
                        logger.error("send error" + e.getMessage());
                    }else {
                        logger.debug("offset:{},partition:{}",recordMetadata.offset(),recordMetadata.partition());
                    }
                }
            });
        logger.debug("kafka 消息投递完成");
        producer.close();
        return true;
    }

    @Override
    public Object poll() {
        consumer.subscribe(Arrays.asList(topic));
        ConsumerRecords<String, List<String>> records = consumer.poll(Duration.ofMillis(3000));
        List<String> list=new ArrayList<>();
        records.forEach((ConsumerRecord<String, List<String>> record)->{
            list.addAll(record.value());
        });
        //有消息
        if(records.count() > 0){
            //手动异步提交offset，当前线程提交offset不会阻塞，可以继续处理后面的程序逻辑
            consumer.commitAsync(new OffsetCommitCallback() {
                @Override
                public void onComplete(Map<TopicPartition, OffsetAndMetadata> map, Exception e) {
                    if(e != null){
                        logger.error("Commit falide exception",e);
                    }
                }
            });
        }
        logger.debug("kafka 消息拉取完成,数量{}",list.size());
        consumer.close();
        return list;

    }



}
