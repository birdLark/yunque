package www.larkmidtable.com.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

public class HdfsWriterTest {
    private HdfsWriter hdfsWriter;
    @Before
    public void init() throws URISyntaxException, IOException, InterruptedException {
        // 获取连接集群的地址
        // 创建一个配置文件
        Configuration configuration = new Configuration();
        configuration.set(FileSystem.FS_DEFAULT_NAME_KEY,"hdfs://127.0.0.1:9000");
        //设置配置文件中副本的数量
        configuration.set("dfs.replication", "1");
        configuration.set(hdfsWriter.HADOOP_USER_NAME,"Administrator");
        hdfsWriter = new HdfsWriter(configuration);
    }

    @Test
    public void testReader() {
        hdfsWriter.setWritePath("/my/honghu/honghu1.txt");
        Queue<List<String>> queue = new LinkedBlockingDeque<>();
        List<String> data = new ArrayList<>();
        data.add("1,1,小明,足球,这是一个喜欢运动的小孩");
        data.add("2,1,小东,乒乓球,兴趣广泛");
        data.add("3,2,小红,唱歌,是个唱歌能手");
        queue.add(data);
        hdfsWriter.startWrite(queue);
        hdfsWriter.close();
    }

}
