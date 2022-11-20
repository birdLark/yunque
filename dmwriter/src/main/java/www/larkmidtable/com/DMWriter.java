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
import java.util.List;
import java.util.Queue;

/**
 *
 *
 * @Date: 2022/11/14 11:01
 * @Description:
 **/
public class DMWriter extends Writer {

	private Connection connection ;
	private PreparedStatement statement ;
	private static Logger logger = LoggerFactory.getLogger(DMWriter.class);
	@Override
	public void open() {
		try {
			logger.info("DM的Writer建立连接开始....");
			Class.forName(DBType.DM.getDriverClass());
			connection = DriverManager
					.getConnection(" jdbc:dm://127.0.0.1:5236/DAMENG" ,"SYSDBA","SYSDBA");
			connection.setAutoCommit(false);
			logger.info("DM的Writer建立连接结束....");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void startWrite() {
		logger.info("开始写数据....");
		List<String> poll = Channel.getQueue().poll();
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
			logger.info("DM的Writer开始进行关闭连接开始....");
			statement.close();
			connection.close();
			logger.info("DM的Writer开始进行关闭连接结束....");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
