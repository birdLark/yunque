package www.larkmidtable.com;

import com.alibaba.fastjson.JSONObject;
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
import java.util.concurrent.ArrayBlockingQueue;

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
			logger.info("MySQL的Writer建立连接开始....");
			Class.forName(DBType.MySql.getDriverClass());
			connection = DriverManager.getConnection(configBean.getUrl(),configBean.getUsername(),configBean.getPassword());
			connection.setAutoCommit(false);
			logger.info("MySQL的Writer建立连接结束....");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void startWrite() throws InterruptedException {
		logger.info("开始写数据....");
		List<String> poll = ((ArrayBlockingQueue<List<String>>)Channel.getQueue()).take();
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
				if (i % 10000 == 0) {
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
			logger.info("MySQL的Writer开始进行关闭连接开始....");
			statement.close();
			connection.close();
			logger.info("MySQL的Writer开始进行关闭连接结束....");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
