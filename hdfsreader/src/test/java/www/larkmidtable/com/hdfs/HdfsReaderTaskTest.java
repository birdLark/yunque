package www.larkmidtable.com.hdfs;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
/**
 * JorkJoin作业单元测试
 * @Author daizhong.liu
 * @Date 2022-11-24 14:45:17
 **/
@Slf4j
public class HdfsReaderTaskTest {
    List<HdfsReaderParams> params;
    private ForkJoinPool forkJoinPool;//线程池
    @Before
    public void init() {
        forkJoinPool = new ForkJoinPool(5);
        params = new ArrayList<>();
        for(int i=1;i<=20;i++) {
            HdfsReaderParams hdfsReaderParams1 = new HdfsReaderParams();
            hdfsReaderParams1.setHdfsUrl("hdfs://127.0.0.1:9000");
            hdfsReaderParams1.setUsername("Administrator");
            hdfsReaderParams1.setPath("/my/honghu/honghu"+i+".txt");
            params.add(hdfsReaderParams1);
        }


    }
    /**
     * 根据从HDFS中多个文件中抽取数据，数据格式为：1\0011\001小明\001足球\001这是一个喜欢运动的小孩.\n2\0011\001小东\001乒乓球\001兴趣广泛\n
     * 多线程读取后，转读取的数据转成统一格式，统一数据格式为：1|1|小明|足球|这是一个喜欢运动的小孩.\n2|1|小东|乒乓球|兴趣广泛\n，再投递回MQ中
     * @Author daizhong.liu
     * @Date 2022-11-24 14:54:13
     **/
    @Test
    public void test() {
        HdfsReaderTask readerTask = new HdfsReaderTask(HdfsReaderTask.DEFAULT_ROW_DELIMITER,HdfsReaderTask.DEFAULT_COL_DELIMITER);
        readerTask.setTaskParams(params);
        readerTask.setSingleThreadSize(2);
        ForkJoinTask<List<String>> submit = forkJoinPool.submit(readerTask);
        try {
            long start = System.currentTimeMillis();
            List<String> result = submit.get();
            System.err.println("耗时:"+(System.currentTimeMillis() - start)+"ns,结果集大小:"+result.size());
//            System.err.println(JSONObject.toJSONString(result));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
