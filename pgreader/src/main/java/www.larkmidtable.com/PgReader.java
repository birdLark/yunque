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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 *
 * @Date: 2022/11/14 11:01
 * @Description:
 **/
public class PgReader extends Reader {

	private Connection connection ;
	private PreparedStatement statement ;
	private static Logger logger = LoggerFactory.getLogger(PgReader.class);
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
	public Queue<List<String>> startRead(String[] inputSplits) {
		logger.info("PostgreSQL读取数据操作....");
		try {
			List<String> records =  new ArrayList<>();
			String sql = "select * from test_table";
			statement = connection.prepareCall(sql);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				String name = resultSet.getString("name");
				records.add(name);
			}
			Channel.getQueue().add(records);
		}catch (Exception e){
			e.printStackTrace();
		}
		logger.info("PostgreSQL读取数据结束....");
		return Channel.getQueue();
	}


	@Override
	public String[] createInputSplits() {
		logger.info("PostgreSQL的Reader开始进行分片开始....");
		logger.info("PostgreSQL的Reader开始进行分片结束....");
		return new String[5];
	}

	@Override
	public void close()  {
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
