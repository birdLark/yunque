package www.larkmidtable.com;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import www.larkmidtable.com.util.DBUtil;
import www.larkmidtable.com.writer.Writer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.Queue;

public class DB2Writer extends Writer {

    private Connection connection;
    private PreparedStatement statement;
    private static Logger logger = LoggerFactory.getLogger(DB2Writer.class);

    @Override
    public void open() {
        logger.info("DB2的Writer建立连接开始....");
        try {
            Properties properties = new Properties();
            properties.setProperty("user", "db2admin");
            properties.setProperty("password", "db2admin");

            connection = DriverManager
                    .getConnection("jdbc:db2://192.168.2.8:50000/sample", properties);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.info("DB2的Writer建立连接结束....");
    }

    @Override
    public void startWrite(Queue<List<String>> queue) {
        logger.info("DB2开始写数据....");
        List<String> poll = queue.poll();
        String sql = "insert into GOOCHSAMA.ACT(ACTNO,ACTKWD,ACTDESC) values (?,?,?)";
        try {
            // 批量插入时ps对象必须放到for循环外面
            statement = connection.prepareStatement(sql);
            for (int i = 0; i < poll.size(); i++) {
                statement.setString(1, poll.get(i));
                statement.setString(2, "mary_" + i);
                statement.setString(3, "mary_" + i);
                statement.addBatch();
                if (i % 10000 == 0) {
                    statement.executeBatch();
                    connection.commit();
                    statement.clearBatch();
                }
            }
            statement.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("DB2写数据完成....");
    }

    @Override
    public void close() {
        logger.info("DB2的Writter开始进行关闭连接开始....");
        DBUtil.close(statement, connection);
        logger.info("DB2的Writer开始进行关闭连接结束....");
    }
}
