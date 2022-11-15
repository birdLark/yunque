package www.larkmidtable.com.writer.doriswriter;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import www.larkmidtable.com.writer.Writer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
/**
 建表语句
 CREATE TABLE stream_test (
 id bigint(20) COMMENT "",
 id2 bigint(20) COMMENT "",
 username varchar(32) COMMENT ""
 ) ENGINE=OLAP
 DUPLICATE KEY(id)
 DISTRIBUTED BY HASH(`id`) BUCKETS 20
 PROPERTIES (
 "replication_num" = "1"
 );
 */

/**
 * @ProjectName: honghu
 * @Package: www.larkmidtable.com.writer.doriswriter
 * @ClassName: DorisWriter
 * @Description: []
 * @Author: [Tony]
 * @Date: 2022/11/15 14:32
 * @Version: V1.0
 **/
public class DorisWriter extends Writer {

    private static Logger logger = LoggerFactory.getLogger(DorisWriter.class);

    //doris地址
    private final static String DORIS_HOST = "172.29.49.218";
    //doris端口
    private final static int DORIS_HTTP_PORT = 8030;
    //目标数据库
    private final static String DORIS_DB = "test_db";
    //目标表
    private final static String DORIS_TABLE = "stream_test";
    //用户
    private final static String DORIS_USER = "root";
    //密码
    private final static String DORIS_PASSWORD = "qazwsx@123";

    private HttpClientBuilder httpClientBuilder;

    private String loadUrl;



    private String basicAuthHeader(String username, String password) {
        final String tobeEncode = username + ":" + password;
        byte[] encoded = Base64.encodeBase64(tobeEncode.getBytes(StandardCharsets.UTF_8));
        return "Basic " + new String(encoded);
    }

    public static void main(String[] args) throws Exception {
        DorisWriter dorisWriter = new DorisWriter();
        Queue<List<String>> queue= new ArrayDeque();
        ArrayList<String> strings = new ArrayList<>();
        for(int i=0;i<3;i++){
            strings.add(i+"");
        }
        queue.add(strings);
        dorisWriter.open();
        dorisWriter.startWrite(queue);
    }

    @Override
    public void open() {
        logger.info("Doris的Writer建立连接开始....");
         loadUrl = String.format("http://%s:%s/api/%s/%s/_stream_load",
                DORIS_HOST,
                DORIS_HTTP_PORT,
                DORIS_DB,
                DORIS_TABLE);

         httpClientBuilder = HttpClients
                .custom()
                .setRedirectStrategy(new DefaultRedirectStrategy() {
                    @Override
                    protected boolean isRedirectable(String method) {
                        return true;
                    }
                });

        logger.info("Doris的Writer建立连接结束....");
    }

    @Override
    public void startWrite(Queue<List<String>> queue) {
        logger.info("Doris开始写数据....");
        //整合数据
        String content = getDorisData(queue);
        //发送数据
        sendDorisDatas(content);
        logger.info("Doris写数据完成....");
    }

    private void sendDorisDatas(String content) {
        try (CloseableHttpClient client = httpClientBuilder.build()) {
            HttpPut put = new HttpPut(loadUrl);
            StringEntity entity = new StringEntity(content, "UTF-8");
            put.setHeader(HttpHeaders.EXPECT, "100-continue");
            put.setHeader(HttpHeaders.AUTHORIZATION, basicAuthHeader(DORIS_USER, DORIS_PASSWORD));
            //标识必填，只能用一次
            put.setHeader("label", UUID.randomUUID().toString());
            put.setEntity(entity);

            try (CloseableHttpResponse response = client.execute(put)) {
                String loadResult = "";
                if (response.getEntity() != null) {
                    loadResult = EntityUtils.toString(response.getEntity());
                }
                final int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != 200) {
                    throw new IOException(
                            String.format("Stream load failed, statusCode=%s load result=%s", statusCode, loadResult));
                }

                System.out.println(loadResult);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getDorisData(Queue<List<String>> queue) {
        List<String> poll = queue.poll();
        StringBuilder stringBuilder = new StringBuilder();
        for (String content:poll){
            int id1 = 1;
            int id2 = 10;
            String id3 = "张三";
            int rowNumber = 10;
            String oneRow = id1 + "\t" + id2 + "\t" + id3 + "\n";
            stringBuilder.append(oneRow);
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        String content = stringBuilder.toString();
        return content;
    }

    @Override
    public void close() {
        logger.info("Doris的Writter开始进行关闭连接开始....");
        logger.info("Doris的Writer开始进行关闭连接结束....");
    }
}
