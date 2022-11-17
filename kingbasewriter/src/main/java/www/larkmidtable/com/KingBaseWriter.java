package www.larkmidtable.com;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import www.larkmidtable.com.util.DBType;
import www.larkmidtable.com.writer.Writer;

import java.sql.*;
import java.util.List;
import java.util.Queue;

/**
 * @projectName: honghu
 * @package: www.larkmidtable.com
 * @className: KingBaseReader
 * @author: qd.liu
 * @description: 人大金仓读组件
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
                    .getConnection("jdbc:kingbase8://127.0.0.1:54321/test", "system", "123456");
            connection.setAutoCommit(false);
            logger.info("KingBase的Writer建立连接结束....");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startWrite(Queue<List<String>> queue) {
        logger.info("开始写数据....");
        List<String> poll = queue.poll();
        String sql = "insert into student(id,name) values (?,?)";
        try {
            statement = connection.prepareStatement(sql); // 批量插入时ps对象必须放到for循环外面
            for (int i = 0; i < poll.size(); i++) {
                statement.setString(1, "mary_" + i);
                statement.setString(2, poll.get(i));
                statement.addBatch();
                if (i % 10000 == 0) {
                    statement.executeBatch();
                    connection.commit();
                    statement.clearBatch();
                }
            }
            statement.executeBatch();
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
