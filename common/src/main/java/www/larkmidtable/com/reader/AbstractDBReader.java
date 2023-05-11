package www.larkmidtable.com.reader;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import www.larkmidtable.com.channel.Channel;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public abstract class AbstractDBReader extends Reader {

	private static Logger logger = LoggerFactory.getLogger(AbstractDBReader.class);
    /**
     * 拆分批量默认值，可考虑覆盖配置
     */
    protected final Integer DEFAULT_BATCH_SIZE = 10000;

    /**
     * 获取总条数
     * 目的是做SQL拆分
     *
     * @return
     */
    public abstract int count();

    /**
     * 开启批量多线程读
     *
     * @param connection
     * @param inputSplits
     * @return
     */
    public void batchStartRead(Connection connection, String[] inputSplits) throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(inputSplits.length);
        for (int i = 0; i < inputSplits.length; i++) {
            String inputSplit = inputSplits[i];
            // 后续可改成线程池
            new Thread(() -> {
                try {
                    defaultSingleStartRead(connection, inputSplit);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                } finally {
                    countDownLatch.countDown();
                }
            }).start();
        }
        // 等待所有的子线程运行完毕
        countDownLatch.await();
        // 发送
        // TODO 是否发送读取结束标识符，告诉下游？
    }

    /**
     * 默认的线程读写处理逻辑，数据库可选择覆写逻辑，实现自己的读写
     *
     * @param connection
     * @param inputSplit
     * @throws Exception
     */
    public void defaultSingleStartRead(Connection connection, String inputSplit) throws Exception {
        List<String> records = new ArrayList<>();
        // String sql = String.format("select * from %s", configBean.getTable());
		PreparedStatement preparedStatement = connection.prepareStatement(inputSplit);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
        	Map<String ,Object> map = new HashMap<>();
			ResultSetMetaData metaData = resultSet.getMetaData();
			for(int i =1;i<=metaData.getColumnCount();i++) {
				map.put(metaData.getColumnName(i),resultSet.getObject(metaData.getColumnName(i)));
			}
            records.add(JSONObject.toJSONString(map));
        }
		logger.info("添加到队列的记录条数{}",records.size());
		//责任链模式执行数据清洗/转换
        if(records.size() != 0){
            Channel.getQueue().add(records);
        }
    }

    public List<String> defaultInputSplits(String column,String originInput) {
        List<String> splits = new ArrayList<>();
        int count = count();
        if (count > 0 && 1 == 1) {// 1==1 后续可开启切分SQL配置参数
            // 拆分的大小
            int size = this.getConfigBean().getThread();
            int lastCount = count % DEFAULT_BATCH_SIZE;
            for (int i = 0; i < size; i++) {
                StringBuilder builder = new StringBuilder("SELECT "+column+" FROM ( ");
                builder.append(" ").append(originInput).append(" ) t").append(" ").append("LIMIT");
                int limitStart = i * DEFAULT_BATCH_SIZE;
                int j = i + 1;
                if (j == size) {
                    builder.append(" ").append(limitStart).append(",").append(lastCount);
                } else {
                    builder.append(" ").append(limitStart).append(",").append(DEFAULT_BATCH_SIZE);
                }
                splits.add(builder.toString());
            }
        } else {
            splits.add(originInput);
        }
        return splits;
    }
}
