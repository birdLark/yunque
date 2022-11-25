package www.larkmidtable.com.hdfs;

import lombok.Getter;
import lombok.Setter;
import www.larkmidtable.com.concurrent.TaskParams;

@Getter
@Setter
public class HdfsReaderParams extends TaskParams {
    private String hdfsUrl;
    private String username;
    private String path;
}
