package www.larkmidtable.com;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import www.larkmidtable.com.writer.Writer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Queue;

/**
 *
 *
 * @Date: 2022/11/14 11:01
 * @Description:
 **/
public class MySQLWriter extends Writer {

	private Connection connection ;
	private PreparedStatement statement ;
	private static Logger logger = LoggerFactory.getLogger(MySQLWriter.class);
	@Override
	public void open() {
		try {
			logger.info("MySQL建立连接开始....");
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager
					.getConnection("jdbc:mysql://localhost:3306/filedb?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC","root","root");
			connection.setAutoCommit(false);
			logger.info("MySQL建立连接结束....");
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
			logger.info("MySQL开始进行关闭连接开始....");
			statement.close();
			connection.close();
			logger.info("MySQL开始进行关闭连接结束....");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
