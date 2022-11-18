package www.larkmidtable.com;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import www.larkmidtable.com.channel.Channel;
import www.larkmidtable.com.reader.Reader;
import www.larkmidtable.com.util.DBType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * @Description: [dorisread]
 * @Title:
 * @Author: tony
 * @Date: 2022-11-17
 * @Param: dorisread
 * @Return: null
 * @Throws:
 */
public class DorisReader extends Reader {

	private Connection connection;
	private PreparedStatement statement;
	private static Logger logger = LoggerFactory.getLogger(DorisReader.class);

	@Override
	public void open() {
		try {
			logger.info("Doris的Reader建立连接开始....");
			Class.forName(DBType.MySql.getDriverClass());
			connection = DriverManager
					.getConnection("jdbc:mysql://xxx.xx.xx.xxx:9030/test_db?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC", "root", "xxxx");
			logger.info("Doris的Reader建立连接结束....");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Queue<List<String>> startRead(String[] inputSplits) {
		logger.info("Doris读取数据操作....");
		try {
			List<String> records = new ArrayList<>();
			String sql = "select username from stream_test";
			statement = connection.prepareCall(sql);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				String name = resultSet.getString("username");
				logger.info(name);
				records.add(name);
			}
			Channel.getQueue().add(records);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Doris读取数据结束....");
		return Channel.getQueue();
	}


	@Override
	public String[] createInputSplits() {
		logger.info("Doris的Reader开始进行分片开始....");
		logger.info("Doris的Reader开始进行分片结束....");
		return new String[5];
	}

	@Override
	public void close() {
		try {
			logger.info("Doris的Reader开始进行关闭连接开始....");
			statement.close();
			connection.close();
			logger.info("Doris的Reader开始进行关闭连接结束....");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		DorisReader dorisReader = new DorisReader();
		dorisReader.open();
		String[] arrays= new String['a'];
		dorisReader.startRead(arrays);
	}
}

