package www.larkmidtable.com.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import www.larkmidtable.com.concurrent.ForkJoinRecursiveTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author daizhong.liu
 * @description TODO
 * @date 2022/11/25 0025 14:46
 */
public class HdfsWriterTask extends ForkJoinRecursiveTask<HdfsWriterParams,String> {
    public static String DEFAULT_ROW_DELIMITER = "\n";
    public static String DEFAULT_COL_DELIMITER = "\001";//列分隔符

    private String lineSplit;
    private String columnSplit;

    public HdfsWriterTask(String lineSplit, String columnSplit) {
        this.lineSplit = lineSplit;
        this.columnSplit = columnSplit;
    }

    @Override
    protected ForkJoinRecursiveTask<HdfsWriterParams, String> buildTask() {
        return new HdfsWriterTask(this.lineSplit,this.columnSplit);
    }

    @Override
    public List<String> doProcess(List<HdfsWriterParams> params) {
        List<String> data = new ArrayList<>();
        params.stream().forEach(param -> {
            Configuration configuration = new Configuration();
            configuration.set(FileSystem.FS_DEFAULT_NAME_KEY, param.getHdfsUrl());
            try {
                FileSystem fs = FileSystem.get(configuration);
                Properties properties = System.getProperties(); //这两行告诉hadoop访问时的用户
                properties.setProperty("HADOOP_USER_NAME", param.getUsername());
                List<String> result = dataWrite(param.getPath(), fs, param.getQueueData());
                data.addAll(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return data;
    }
    public List<String> dataWrite(String path,FileSystem fs,List<String> datas) {
        List<String> result = new ArrayList<>();
        Path destPath = new Path(path);
        Path finalPath = new Path(fs.makeQualified(destPath).toUri());
        try {
            DataOutputStream writer = fs.create(finalPath, true, datas.size());
            for (int i = 0; i < datas.size(); i++) {
                String record = datas.get(i);
                String[] columns = record.split(this.columnSplit);
                String newRecord = String.join(DEFAULT_COL_DELIMITER, columns) + DEFAULT_ROW_DELIMITER;
                writer.write(newRecord.getBytes("UTF-8"));
                result.add(newRecord);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
