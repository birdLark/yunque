package www.larkmidtable.com.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import www.larkmidtable.com.writer.Writer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Queue;

public class HdfsWriter extends Writer {
    public static final String HADOOP_USER_NAME = "HADOOP_USER_NAME";
    public static String RECORD_BATCH_SIZE = "record.batch.size";
    public static int DEFAULT_BATCH_SIZE = 10000;
    private FileSystem fs;
    private Configuration conf;
    private DataOutputStream writer;
    private String writePath;
    private int batchSize ;

    public HdfsWriter(Configuration conf) throws IOException {
        this.conf = conf;
        fs = FileSystem.get(conf);
        Properties properties = System.getProperties(); //这两行告诉hadoop访问时的用户为root
        properties.setProperty(HADOOP_USER_NAME, conf.get(HADOOP_USER_NAME));
        this.batchSize = conf.getInt(RECORD_BATCH_SIZE, DEFAULT_BATCH_SIZE);
    }

    @Override
    public void open() {

    }

    @Override
    public void startWrite(Queue<List<String>> queue) {
        List<String> poll = queue.poll();
        Path destPath = new Path(writePath);
        Path finalPath = new Path(fs.makeQualified(destPath).toUri());
        try {
            this.writer = fs.create(finalPath, true, batchSize);
            for (int i = 0; i < poll.size(); i++) {
                String record = poll.get(i);
                this.writer.write(record.getBytes("UTF-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            writer.flush();
            writer.close();
            fs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setWritePath(String writePath) {
        this.writePath = writePath;
    }
}
