package www.larkmidtable.com;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import www.larkmidtable.com.channel.Channel;
import www.larkmidtable.com.util.DBType;
import www.larkmidtable.com.util.DBUtil;
import www.larkmidtable.com.writer.Writer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;

public class DB2Writer extends Writer {

    private Connection connection;
    private PreparedStatement statement;
    private static final Logger logger = LoggerFactory.getLogger(DB2Writer.class);

    @Override
    public void open() {
        try {
            logger.info("DB2的Writer建立连接开始....");
            Class.forName(DBType.DB2.getDriverClass());
            connection = DriverManager.getConnection(configBean.getUrl(),configBean.getUsername(),configBean.getPassword());
            connection.setAutoCommit(false);
            logger.info("DB2的Writer建立连接结束....");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startWrite() {
        logger.info("DB2开始写数据....");
        List<String> poll = Channel.getQueue().poll();
        String sql = String.format("insert into %s(%s) values (?,?)",configBean.getTable(),configBean.getColumn());
        try {
            // 批量插入时ps对象必须放到for循环外面
            statement = connection.prepareStatement(sql);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("DB2写数据完成....");
    }

    @Override
    public void close() {
        logger.info("DB2的Writer开始进行关闭连接开始....");
        DBUtil.close(statement, connection);
        logger.info("DB2的Writer开始进行关闭连接结束....");
    }
}
