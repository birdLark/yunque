package www.larkmidtable.com.channel;



import www.larkmidtable.com.queue.DefaultQueue;
import www.larkmidtable.com.transformer.TransformerExecution;

import java.util.List;
import java.util.Queue;

/**
 * @Description:  kafka通道
 * @Date: 2022/12/2 11:43
 */
public class DefaultChannel extends  Channel {

    public DefaultChannel(List<TransformerExecution> transformerExecutionList ) {
        Queue<List<String>> queue = new DefaultQueue(transformerExecutionList);
        this.setQueue(queue);
    }
}
