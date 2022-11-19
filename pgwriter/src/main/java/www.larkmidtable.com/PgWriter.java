package www.larkmidtable.com;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import www.larkmidtable.com.channel.Channel;
import www.larkmidtable.com.util.DBType;
import www.larkmidtable.com.writer.Writer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 *
 * @Date: 2022/11/14 11:01
 * @Description:
 **/
public class PgWriter extends Writer {

	private Connection connection ;
	private PreparedStatement statement ;
	private static Logger logger = LoggerFactory.getLogger(PgWriter.class);
	@Override
	public void open() {
		try {
			logger.info("PostgreSQL的Reader建立连接开始....");
			Class.forName(DBType.PostgreSql.getDriverClass());
			connection = DriverManager
					.getConnection("jdbc:postgresql://127.0.0.1:5432/test","postgres","123321");
			logger.info("PostgreSQL的Reader建立连接结束....");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void startWrite() {
		logger.info("开始写数据....");
		List<String> poll = Channel.getQueue().poll();
		String sql = "insert into test_table(name,remark) values (?,?)";
		try {
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(sql); // 批量插入时ps对象必须放到for循环外面
			for (int i = 0; i < poll.size(); i++) {
				statement.setString(1, "mary_" + i);
				statement.setString(2, poll.get(i));
				statement.addBatch();
				if (i % 3 == 0) {
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
			logger.info("PostgreSQL的Reader开始进行关闭连接开始....");
			statement.close();
			connection.close();
			logger.info("PostgreSQL的Reader开始进行关闭连接结束....");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
