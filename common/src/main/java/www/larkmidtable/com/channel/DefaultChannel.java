package www.larkmidtable.com.channel;



import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Description:  kafka通道
 * @Date: 2022/12/2 11:43
 */
public class DefaultChannel extends  Channel {

    public DefaultChannel() {
        Queue<List<String>> queue = new LinkedBlockingQueue<List<String>>();
        this.setQueue(queue);
    }
}
