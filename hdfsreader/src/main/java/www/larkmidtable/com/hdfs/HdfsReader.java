package www.larkmidtable.com.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import www.larkmidtable.com.channel.Channel;
import www.larkmidtable.com.reader.Reader;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Queue;

/**
 * HDFS读取器，一般来说hdfs的读取通常是采用hadoop批处理的方式，继承RecordReader来实现小文件合并读取，多任务批量读取再轮询每行进行数据解析
 */
public class HdfsReader extends Reader{
    public static final String HADOOP_USER_NAME = "HADOOP_USER_NAME";
    private FileSystem fs;
    private Configuration conf;
    private String readPath;

    public HdfsReader(Configuration conf) throws IOException {
        this.conf = conf;
        fs = FileSystem.get(conf);
        Properties properties = System.getProperties(); //这两行告诉hadoop访问时的用户
        properties.setProperty(HADOOP_USER_NAME, conf.get(HADOOP_USER_NAME));
    }

    public FileSystem getFs() {
        return fs;
    }

    @Override
    public void open() {

    }

    @Override
    public Queue<List<String>> startRead(String[] inputSplits) {
        Path readPath = new Path(this.readPath);
        try {
            FSDataInputStream open = fs.open(readPath);
            byte[] buffer = new byte[1024];//数组
            List<String> data = new ArrayList<>();
            while((open.read(buffer))!=-1){
                String read = new String(buffer, Charset.forName("UTF-8"));
                data.add(read);
            }
			Channel.getQueue().add(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Channel.getQueue();
    }

    @Override
    public Queue<List<String>> startRead(String inputSplit) {
        return null;
    }


    @Override
    public String[] createInputSplits() {
        return new String[0];
    }

    @Override
    public void close() {
        try {
            fs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 由于父类没有参数传入，因此在读取前需设置读取路径
     * @param readPath
     */
    public void setReadPath(String readPath) {
        this.readPath = readPath;
    }
}
