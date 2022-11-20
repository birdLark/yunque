package www.larkmidtable.com.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

public class HdfsReaderTest {
    private HdfsReader hdfsReader;
    @Before
    public void init() throws URISyntaxException, IOException, InterruptedException {
        // 获取连接集群的地址
        // 创建一个配置文件
        Configuration configuration = new Configuration();
        configuration.set(FileSystem.FS_DEFAULT_NAME_KEY,"hdfs://127.0.0.1:9000");
        //设置配置文件中副本的数量
        configuration.set("dfs.replication", "1");
        configuration.set(HdfsReader.HADOOP_USER_NAME,"Administrator");
        hdfsReader = new HdfsReader(configuration);
    }

    @Test
    public void testReader() {
        hdfsReader.setReadPath("/my/honghu/honghu1.txt");
        Queue<List<String>> queue = hdfsReader.startRead(null);
        hdfsReader.close();
        List<String> poll = queue.poll();
        poll.stream().forEach(System.out::println);
    }

    @Test
    public void testmkdir() throws URISyntaxException, IOException, InterruptedException {
        // 创建一个文件夹
        hdfsReader.getFs().mkdirs(new Path("/my/demo"));
    }

    // 上传操作


    /**
     * 参数优先级
     * 代码里面 > 项目资源目录下的配置文件(resources/hdfs-site.xml) > linux中hdfs配置文件
     */
    @Test
    public void testPut() throws IOException {
        // 参数解读：参数一：表示删除元数据  参数二：是否允许覆盖  参数三：元数据路径  参数四：目的地路径
        hdfsReader.getFs().copyFromLocalFile(false, true, new Path("D:\\honghu.txt"), new Path("hdfs://127.0.0.1:9000/my/honghu"));
    }


    @Test
    public void testGet() throws IOException {
        /**
         *  参数解读： 参数一：源文件是否删除  参数二：源文件的路径  参数三：目标路径  参数四：是否关闭校验
         */
        hdfsReader.getFs().copyToLocalFile(false, new Path("hdfs://127.0.0.1:9000/my/demo/honghu"), new Path("D:\\honghu2.txt"), true);
    }


    //删除
    @Test
    public void testRm() throws IOException {
        /**
         *  参数解读： 参数一：要删除的路径  参数二：是否递归删除
         */
        // 删除文件
        //fs.delete(new Path("hdfs://hadoop102/jdk-8u212-linux-x64.tar.gz"),false);

        // 删除空目录 加不加hdfs一样的
        //fs.delete(new Path("/xiyou"),false);

        //删除非空目录
        hdfsReader.getFs().delete(new Path("hdfs://127.0.0.1:9000/my/demo/honghu"), true);
    }


    //文件及文件夹的更名和移动
    @Test
    public void testMv() throws IOException {
        /**
         *  参数解读： 参数一：源文件路径  参数二：目标文件的路径
         */
        //对文件名称的修改
        //fs.rename(new Path("hdfs://hadoop102/sanguo/zhangfei.txt"),new Path("hdfs://hadoop102/sanguo/guanyu.txt"));

        //文件的更名和移动
        //fs.rename(new Path("hdfs://hadoop102/sanguo/guanyu.txt"),new Path("hdfs://hadoop102/dianwei.txt"));
        // 文件夹更名
        hdfsReader.getFs().rename(new Path("/my/demo"), new Path("/my/honghu"));
    }


    //获取文件详细信息
    @Test
    public void fileDetail() throws IOException {
        // 获取所有文件信息
        RemoteIterator<LocatedFileStatus> listFiles = hdfsReader.getFs().listFiles(new Path("/"), true);

        // 遍历迭代器
        while (listFiles.hasNext()) {
            LocatedFileStatus fileStatus = listFiles.next();

            System.out.println("==========" + fileStatus.getPath() + "==========");
            System.out.println(fileStatus.getPermission());
            System.out.println(fileStatus.getOwner());
            System.out.println(fileStatus.getGroup());
            System.out.println(fileStatus.getLen());
            System.out.println(fileStatus.getModificationTime());
            System.out.println(fileStatus.getReplication());
            System.out.println(fileStatus.getBlockSize());
            System.out.println(fileStatus.getPath().getName());

            // 获取块信息
            BlockLocation[] blockLocations = fileStatus.getBlockLocations();
            System.out.println(Arrays.toString(blockLocations));
        }
    }


    // 判断是文件夹还是文件
    @Test
    public void testFile() throws IOException {
        FileStatus[] listStatus = hdfsReader.getFs().listStatus(new Path("/"));
        for (FileStatus status : listStatus) {
            if (status.isFile()) {
                System.out.println("文件:" + status.getPath().getName());
            } else {
                System.out.println("目录:" + status.getPath().getName());
            }
        }
    }
}
