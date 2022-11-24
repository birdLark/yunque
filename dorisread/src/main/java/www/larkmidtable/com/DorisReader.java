package www.larkmidtable.com;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import www.larkmidtable.com.channel.Channel;
import www.larkmidtable.com.reader.AbstractDBReader;
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
public class DorisReader extends AbstractDBReader {

	private Connection connection;
	//private PreparedStatement statement;
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
			if (inputSplits.length > 1) {
				// 开启多线程肚
				batchStartRead(connection, inputSplits);
			} else {
				defaultSingleStartRead(connection, inputSplits[0]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Doris读取数据结束....");
		return Channel.getQueue();
	}


	@Override
	public String[] createInputSplits() {
		logger.info("Doris的Reader开始进行分片开始....");
		String inputSql = String.format("select %s from %s",configBean.getColumn(), configBean.getTable());
		List<String> results = defaultInputSplits(configBean.getColumn(),inputSql);
		logger.info("Doris的Reader开始进行分片结束....");
		String[] array = new String[results.size()];
		return results.toArray(array);
	}

	@Override
	public void close() {
		try {
			logger.info("Doris的Reader开始进行关闭连接开始....");
			connection.close();
			logger.info("Doris的Reader开始进行关闭连接结束....");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public int count() {
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement =
					connection.prepareStatement("SELECT count(*) FROM " + configBean.getTable());
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			return resultSet.getInt(1);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return 0;
	}

	public static void main(String[] args) {
		DorisReader dorisReader = new DorisReader();
		dorisReader.open();
		String[] arrays= new String['a'];
		dorisReader.startRead(arrays);
	}
}

