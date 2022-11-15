package www.larkmidtable.com.writer.sqlserverwriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import www.larkmidtable.com.util.DBType;
import www.larkmidtable.com.util.DBUtil;
import www.larkmidtable.com.writer.Writer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Queue;

/**
 * @author fei
 * @description: sqlserver 写工具
 * @date 2022-11-15
 */
public class SqlServerWriter extends Writer {
    private Connection connection ;
    private PreparedStatement statement ;
    private static Logger logger = LoggerFactory.getLogger(SqlServerWriter.class);
    @Override
    public void open() {
        logger.info("SqlServer的Writer建立连接开始....");
        try {
            String url="jdbc:sqlserver://localhost:1433;DatabaseName=ILS";
            String username="manh";
            String password="1qaz@WSX";
            DBUtil.getConnection(DBType.SQLSERVER.getDriverClass(),url,username,password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.info("SqlServer的Writer建立连接结束....");
    }

    @Override
    public void startWrite(Queue<List<String>> queue) {
        logger.info("SqlServer开始写数据....");
        List<String> poll = queue.poll();
        String sql = "insert into student(id,name) values (?,?)";
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
        }catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("SqlServer写数据完成....");
    }

    @Override
    public void close() {
        logger.info("SqlServer的Writter开始进行关闭连接开始....");
        DBUtil.close(statement,connection);
        logger.info("SqlServer的Writer开始进行关闭连接结束....");
    }
}
