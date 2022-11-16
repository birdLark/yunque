package www.larkmidtable.com.reader.esreader;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import www.larkmidtable.com.reader.Reader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * @author chenlin
 * @PackageName:www.larkmidtable.com.reader.esreader
 * @ClassName: ESReader
 * @Description:
 * @date 2022/11/15 14:53
 */
public class ESReader extends Reader {

    private static Logger logger = LoggerFactory.getLogger(ESReader.class);


    @Override
    public void open() {
        logger.info("ES 开始连接....");
        ESConfig.restHighLevelClient("localhost");
    }

    @Override
    public Queue<List<String>> startRead(String[] inputSplits) {
        logger.info("ES读取数据操作....");
        List<String> records = new ArrayList<>();
        String index = "doc_1_index";
        GetRequest getRequest = new GetRequest(index);
        try {
            records.add(getClient().get(getRequest, RequestOptions.DEFAULT).getSourceAsString());
        } catch (Exception var8) {
            logger.error("ES读取数据 error:", var8);
        }

        getQueue().add(records);

        return getQueue();
    }


    @Override
    public String[] createInputSplits() {
        logger.info("ES的Reader开始进行分片开始....");
        logger.info("ES的Reader开始进行分片结束....");
        return new String[5];
    }

    @Override
    public void close()  {
        logger.info("ES 开始关闭....");
        try {
			ESConfig.client.close();
		}catch (Exception e) {
        	e.printStackTrace();
		}

    }

    private static RestHighLevelClient getClient() {
        return ESConfig.client;
    }


}
