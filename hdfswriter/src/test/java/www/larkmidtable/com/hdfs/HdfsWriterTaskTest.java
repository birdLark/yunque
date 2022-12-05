package www.larkmidtable.com.hdfs;/**
 * @description TODO
 * @author daizhong.liu
 * @date 2022/11/24 0024 15:49
 */

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

/**
 * @Description TODO
 * @Author daizhong.liu
 * @Date 2022/11/24 0024 15:49
 */
@Slf4j
public class HdfsWriterTaskTest {
    List<HdfsWriterParams> params;
    private ForkJoinPool forkJoinPool;//线程池
    @Before
    public void init() {
        forkJoinPool = new ForkJoinPool(3);
        params = new ArrayList<>();
        List<String> data = new ArrayList<>();
        int index = 1;
        Random random = new Random();
        for(int i = 1;i <= 10000; i++) {
            String record = i+"|"+random.nextInt(3)+"|姓名"+i+"|兴趣"+i+"|备注"+i+".";
            data.add(record);
            if(i % 500 == 0) {
                HdfsWriterParams hwp = new HdfsWriterParams();
                hwp.setHdfsUrl("hdfs://127.0.0.1:9000");
                hwp.setUsername("Administrator");
                hwp.setPath("/my/yunque/yunque"+index+".txt");
                hwp.setQueueData(data);
                data = new ArrayList<>();
                index ++;
                params.add(hwp);
            }
        }
    }
    @Test
    public void test() {
        HdfsWriterTask writerTask = new HdfsWriterTask("\n","\\|");
        writerTask.setTaskParams(params);
        //一共20个文件，每条线程处理5个文件
        writerTask.setSingleThreadSize(5);
        ForkJoinTask<List<String>> submit = forkJoinPool.submit(writerTask);
        try {
            long start = System.currentTimeMillis();
            List<String> result = submit.get();
            System.err.println("耗时:"+(System.currentTimeMillis() - start)+"ns,结果集数量:"+result.size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
