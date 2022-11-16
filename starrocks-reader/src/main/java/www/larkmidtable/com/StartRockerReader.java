package www.larkmidtable.com;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import www.larkmidtable.com.reader.Reader;
import www.larkmidtable.com.util.DBType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 *
 *
 * @Date: 2022/11/14 11:01
 * @Description:
 **/
public class StartRockerReader extends Reader {

	private Connection connection ;
	private PreparedStatement statement ;
	private static Logger logger = LoggerFactory.getLogger(StartRockerReader.class);
	@Override
	public void open() {
		try {
			logger.info("MySQL的Reader建立连接开始....");
			Class.forName(DBType.MySql8.getDriverClass());
			connection = DriverManager
					.getConnection("jdbc:mysql://localhost:3306/filedb?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC","root","root");
			logger.info("MySQL的Reader建立连接结束....");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Queue<List<String>> startRead(String[] inputSplits) {
		logger.info("MySQL读取数据操作....");
		try {
			List<String> records =  new ArrayList<>();
			String sql = "select * from student";
			statement = connection.prepareCall(sql);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				String name = resultSet.getString("name");
				records.add(name);
			}
			getQueue().add(records);
		}catch (Exception e){
			e.printStackTrace();
		}
		logger.info("MySQL读取数据结束....");
		return getQueue();
	}


	@Override
	public String[] createInputSplits() {
		logger.info("MySQL的Reader开始进行分片开始....");
		logger.info("MySQL的Reader开始进行分片结束....");
		return new String[5];
	}

	@Override
	public void close()  {
		try {
			logger.info("MySQL的Reader开始进行关闭连接开始....");
			statement.close();
			connection.close();
			logger.info("MySQL的Reader开始进行关闭连接结束....");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
