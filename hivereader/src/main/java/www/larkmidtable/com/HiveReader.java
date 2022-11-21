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
 *
 *
 * @Date 2022-11-21 14:56
 * @Description:
 **/
public class HiveReader extends Reader {

	private Connection connection ;
	private PreparedStatement statement ;
	private static Logger logger = LoggerFactory.getLogger(HiveReader.class);


	@Override
	public void open() {
		try {
			logger.info("Hive的Reader建立连接开始....");
			Class.forName(DBType.Hive.getDriverClass());
			connection  = DriverManager.getConnection(configBean.getUrl(),configBean.getUsername(),configBean.getPassword());
			logger.info("Hive的Reader建立连接结束....");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Queue<List<String>> startRead(String[] inputSplits) {
		logger.info("Hive读取数据操作....");
		try {
			List<String> records =  new ArrayList<>();
			String sql = String.format("select * from  %s",configBean.getTable());
			statement = connection.prepareCall(sql);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				String name = resultSet.getString(configBean.getColumn());
				records.add(name);
			}
			Channel.getQueue().add(records);
		}catch (Exception e){
			e.printStackTrace();
		}
		logger.info("Hive读取数据结束....");
		return Channel.getQueue();
	}


	@Override
	public String[] createInputSplits() {
		logger.info("Hive的Reader开始进行分片开始....");
		logger.info("Hive的Reader开始进行分片结束....");
		return new String[5];
	}

	@Override
	public void close()  {
		try {
			logger.info("Hive的Reader开始进行关闭连接开始....");
			statement.close();
			connection.close();
			logger.info("Hive的Reader开始进行关闭连接结束....");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
