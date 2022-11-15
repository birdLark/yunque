package www.larkmidtable.com.writer.eswriter;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig.Builder;
import org.elasticsearch.client.*;
import org.elasticsearch.client.RestClient.FailureListener;
import org.elasticsearch.client.RestClientBuilder.RequestConfigCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @PackageName:www.larkmidtable.com.writer.eswriter
 * @ClassName: ESConfig
 * @Description:
 * @author chenlin
 * @date 2022/11/15 14:13
 */
public class ESConfig {
    private static final Logger log = LoggerFactory.getLogger(ESConfig.class);
    public static RestHighLevelClient client;


    public static RestHighLevelClient restHighLevelClient(String address) {
        String[] hosts = address.split(",");
        if (hosts != null && hosts.length > 0) {
            HttpHost[] httpHosts = new HttpHost[hosts.length];
            int count = 0;
            String[] var4 = hosts;
            int var5 = hosts.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                String host = var4[var6];
                httpHosts[count] = new HttpHost(host, 9200, "http");
                ++count;
            }

            RestClientBuilder restClientBuilder = RestClient.builder(httpHosts).setRequestConfigCallback(new RequestConfigCallback() {
                @Override
                public Builder customizeRequestConfig(Builder requestConfigBuilder) {
                    /**
                     * 链接建立的超时时间
                     */
                    requestConfigBuilder.setConnectTimeout(20000);
                    /**
                     * 响应超时时间，超过此时间不再读取响应
                     */
                    requestConfigBuilder.setSocketTimeout(300000);
                    /**
                     * http clilent中从connetcion pool中获得一个connection的超时时间
                     */
                    requestConfigBuilder.setConnectionRequestTimeout(20000);
                    return requestConfigBuilder;
                }
            });
            restClientBuilder.setFailureListener(new FailureListener() {
                @Override
                public void onFailure(Node node) {
                    ESConfig.log.error("************************es 监听器 failure:{}", node.getName());
                    restClientBuilder.setNodeSelector(NodeSelector.SKIP_DEDICATED_MASTERS);
                    client = new RestHighLevelClient(restClientBuilder);
                    ESConfig.log.info("************************es 重连");

                }
            });
            restClientBuilder.setNodeSelector(NodeSelector.SKIP_DEDICATED_MASTERS);
            client = new RestHighLevelClient(restClientBuilder);
            log.info("ElasticSearch client init success ....");
        }

        return client;
    }
}
