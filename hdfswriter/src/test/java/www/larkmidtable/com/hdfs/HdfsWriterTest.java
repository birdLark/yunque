package www.larkmidtable.com.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.junit.Before;
import org.junit.Test;
import www.larkmidtable.com.channel.Channel;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

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
    public void testWrite() {
        hdfsWriter.setWritePath("/my/honghu/honghu1.txt");
        List<String> data = new ArrayList<>();
        data.add("1\0011\001小明\001足球\001这是一个喜欢运动的小孩.\n");
        data.add("2\0011\001小东\001乒乓球\001兴趣广泛\n");
        data.add("3\0012\001小红\001唱歌\001是个唱歌能手\n");
        Channel.getQueue().add(data);
        hdfsWriter.startWrite();

        hdfsWriter.setWritePath("/my/honghu/honghu2.txt");
        data = new ArrayList<>();
        data.add("5\0011\001七工\001足球2\001这是一苦区区.\n");
        data.add("6\0011\001和开\001乒乓球2\001钱列须鹅\n");
        data.add("7\0012\001鼐人\001唱歌2\001鞯作青个\n");
        Channel.getQueue().add(data);
        hdfsWriter.startWrite();


        hdfsWriter.setWritePath("/my/honghu/honghu3.txt");
        data = new ArrayList<>();
        data.add("8\0012\001基\001足球3\001这苦列景.\n");
        data.add("9\0012\001村\001乒乓球3\00142主则\n");
        data.add("10\0010\001只末是\001唱歌3\001税用伤脑筋\n");
        Channel.getQueue().add(data);
        hdfsWriter.startWrite();



        hdfsWriter.setWritePath("/my/honghu/honghu4.txt");
        data = new ArrayList<>();
        data.add("12\0012\001苦载\001足球4\001瞧向\n");
        data.add("13\0013\0012找载\001乒乓球4\001信脾\n");
        data.add("14\0010\001换睥柑\001唱歌4\001为5垢\n");
        Channel.getQueue().add(data);
        hdfsWriter.startWrite();



        hdfsWriter.setWritePath("/my/honghu/honghu5.txt");
        data = new ArrayList<>();
        data.add("24\0010\001磕到\001足球5\001【8得跟.\n");
        data.add("25\0010\001有5用不上\001乒乓球5\0014遥\n");
        data.add("26\0011\001监控一\001唱歌5\001人算用用充偏了\n");
        Channel.getQueue().add(data);
        hdfsWriter.startWrite();


        hdfsWriter.close();
    }

}
