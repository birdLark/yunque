package www.larkmidtable.com.hdfs;

import lombok.Getter;
import lombok.Setter;
import www.larkmidtable.com.concurrent.TaskParams;

import java.util.List;

/**
 * @Description TODO
 * @Author daizhong.liu
 * @Date 2022/11/24 0024 15:16
 */
@Getter
@Setter
public class HdfsWriterParams extends TaskParams {
    private String hdfsUrl;
    private String username;
    private String path;
    private List<String> queueData;
}
