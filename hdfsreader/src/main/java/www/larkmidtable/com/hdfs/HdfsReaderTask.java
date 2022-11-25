package www.larkmidtable.com.hdfs;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import www.larkmidtable.com.channel.Channel;
import www.larkmidtable.com.concurrent.ForkJoinRecursiveTask;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @author daizhong.liu
 * @description TODO
 * @date 2022/11/25 0025 14:29
 */
@Slf4j
public class HdfsReaderTask extends ForkJoinRecursiveTask<HdfsReaderParams,String> {

    public static String DEFAULT_ROW_DELIMITER = "\n";
    public static String DEFAULT_COL_DELIMITER = "\001";//列分隔符

    private String lineSplit;
    private String columnSplit;

    public HdfsReaderTask(String lineSplit, String columnSplit) {
        this.lineSplit = lineSplit;
        this.columnSplit = columnSplit;
    }

    @Override
    protected ForkJoinRecursiveTask<HdfsReaderParams, String> buildTask() {
        return new HdfsReaderTask(lineSplit,columnSplit);
    }

    @Override
    public List<String> doProcess(List<HdfsReaderParams> params) {
        System.err.println("当前线程："+Thread.currentThread().getName()+",处理条数:"+params.size());
        List<String> result = new ArrayList<>();
        params.stream().forEach(param -> {
            Configuration configuration = new Configuration();
            configuration.set(FileSystem.FS_DEFAULT_NAME_KEY, param.getHdfsUrl());
            try {
                FileSystem fs = FileSystem.get(configuration);
                Properties properties = System.getProperties(); //这两行告诉hadoop访问时的用户
                properties.setProperty("HADOOP_USER_NAME", param.getUsername());
                result.addAll(dataRead(param.getPath(), fs));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return result;
    }

    private List<String> dataRead(String path,FileSystem fs) {
        List<String> data = new ArrayList<>();
        Path readPath = new Path(path);
        try {
            FSDataInputStream open = fs.open(readPath);
            byte[] buffer = new byte[1024];//数组
            while((open.read(buffer))!=-1){
                String line = new String(buffer, Charset.forName("UTF-8"));
                String[] realLine = line.trim().split(lineSplit);
                for(int i = 0; i < realLine.length; i++) {
                    String[] columns = realLine[i].split(columnSplit);
                    StringBuilder columnBuilder = new StringBuilder();
                    Arrays.asList(columns).forEach(column -> columnBuilder.append(column).append("|"));
                    data.add(columnBuilder.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Channel.getQueue().add(data);
        return data;
    }
}
