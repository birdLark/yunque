package www.larkmidtable.com.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import www.larkmidtable.com.transformer.TransformerExecution;
import www.larkmidtable.com.transformer.TransformerParameterInfo;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @ClassName: KafkaQueue
 * @Description:
 * @Author: Administrator
 * @Date: 2022/12/2 11:52
 */
public class DefaultQueue extends LinkedBlockingQueue {
    private static Logger logger = LoggerFactory.getLogger(DefaultQueue.class);
    private static List<TransformerExecution> transformerExecutionList = null;

    public DefaultQueue(List<TransformerExecution> transformerExecutionList) {
        this.transformerExecutionList=transformerExecutionList;
    }
    @Override
    public boolean add(Object o) {
        logger.debug("DefaultQueue 消息投递开始");
        final List<String>[] record = new List[]{(List<String>) o};
        if(transformerExecutionList!=null){
            transformerExecutionList.forEach(transformerExecution->{
                TransformerParameterInfo parameter = transformerExecution.getTransformerInfo().getParameter();
                record[0] = transformerExecution.getTransformer().evaluate(record[0], parameter);
            });
        }
        super.add(record[0]);
        logger.debug("DefaultQueue 消息投递完成");
        return true;
    }

    @Override
    public Object poll() {
        Object poll = super.poll();
        return poll;
    }



}
