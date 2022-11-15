package www.larkmidtable.com.writer.eswriter;

import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import www.larkmidtable.com.writer.Writer;

import java.io.IOException;
import java.util.*;

/**
 * @author chenlin
 * @PackageName:www.larkmidtable.com.writer.eswriter
 * @ClassName: ESWriter
 * @Description:
 * @date 2022/11/15 13:37
 */
public class ESWriter extends Writer {

    private static final Logger logger = LoggerFactory.getLogger(ESWriter.class);


    @Override
    public void open() {
        logger.info("ES 开始连接....");
        ESConfig.restHighLevelClient("localhost");
    }


    @Override
    public void startWrite(Queue<List<String>> queue) {
        logger.info("ES开始写数据....");
        List<String> poll = queue.poll();
        String index = null;

        for (int i = 0; i < poll.size(); i++) {
            BulkRequest bulkRequest = new BulkRequest();
            Map<String, Map<String, Object>> esBulkData = new HashMap<>();

            Iterator<Map.Entry<String, Map<String, Object>>> var3 = esBulkData.entrySet().iterator();

            while (var3.hasNext()) {
                Map.Entry<String, Map<String, Object>> oneEsData = var3.next();
                String id = oneEsData.getKey();
                Map<String, Object> esData = oneEsData.getValue();
                UpdateRequest request = new UpdateRequest(index, id);
                request.doc(esData);
                request.docAsUpsert(true);
                bulkRequest.add(request);
            }

            try {
                getClient().bulk(bulkRequest, RequestOptions.DEFAULT);
            } catch (Exception var8) {
                logger.error("save esData error:", var8);
            }
        }
    }

    @Override
    public void close() throws IOException {
        logger.info("ES 开始关闭....");
        ESConfig.client.close();
    }

    private static RestHighLevelClient getClient() {
        return ESConfig.client;
    }
}
