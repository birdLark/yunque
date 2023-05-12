package www.larkmidtable.com;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import www.larkmidtable.com.channel.Channel;
import www.larkmidtable.com.util.DBType;
import www.larkmidtable.com.writer.Writer;

import java.sql.*;
import java.util.List;

/**
 * @projectName: yunque
 * @package: www.larkmidtable.com
 * @className: KingBaseReader
 * @author: qd.liu
 * @description: 人大金仓写组件
 * @date: 2022/11/17 22:09
 * @version: 1.0
 */
public class KingBaseWriter extends Writer {
    private Connection connection ;
    private PreparedStatement statement ;
    private static Logger logger = LoggerFactory.getLogger(KingBaseWriter.class);
    @Override
    public void open() {
        try {
            logger.info("KingBase的Writer建立连接开始....");
            Class.forName(DBType.KingBase8.getDriverClass());
            connection = DriverManager
                    .getConnection(configBean.getUrl(),configBean.getUsername(),configBean.getPassword());
            connection.setAutoCommit(false);
            logger.info("KingBase的Writer建立连接结束....");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startWrite() {
        logger.info("开始写数据....");
        List<String> poll = Channel.getQueue().poll();
        String[] columns = configBean.getColumn().split(",");
        StringBuffer sb=new StringBuffer();
        for(int i =0;i<columns.length;i++) {sb.append("?,");}
        String whstr = sb.toString().substring(0, sb.toString().length() - 1);
        String sql = String.format("insert into %s(%s) values (%s)",configBean.getTable(),configBean.getColumn(),whstr);
        try {
            statement = connection.prepareStatement(sql); // 批量插入时ps对象必须放到for循环外面
            for (int i = 0; i < poll.size(); i++) {
                JSONObject jsonObject = JSONObject.parseObject(poll.get(i));

                for(int j =1;j<=columns.length;j++) {
                    statement.setObject(j,jsonObject.get(columns[j-1]));
                }
                statement.addBatch();
                if (i % 10000 == 0 && i > 0) {
                    statement.executeBatch();
                    connection.commit();
                    statement.clearBatch();
                }
            }
            statement.executeBatch();
            connection.commit();
            statement.clearBatch();
        }catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("写数据完成....");
    }

    @Override
    public void close() {
        try {
            logger.info("KingBase的Writer开始进行关闭连接开始....");
            statement.close();
            connection.close();
            logger.info("KingBase的Writer开始进行关闭连接结束....");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
