package www.larkmidtable.com.reader.sqlserverreader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import www.larkmidtable.com.channel.Channel;
import www.larkmidtable.com.reader.AbstractDBReader;
import www.larkmidtable.com.util.DBType;
import www.larkmidtable.com.util.DBUtil;

/**
 * @author fei
 * @description: sqlserver 读工具
 * @date 2022-11-15
 */
public class SqlServerReader extends AbstractDBReader {
	private Connection connection;
	private static Logger logger = LoggerFactory.getLogger(SqlServerReader.class);

	@Override
	public void open() {
		logger.info("SqlServer的Reader建立连接开始....");
		try {
			connection = DBUtil.getConnection(DBType.SQLSERVER.getDriverClass(), configBean.getUrl(),
					configBean.getUsername(), configBean.getPassword());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		logger.info("SqlServer的Reader建立连接结束....");
	}

	@Override
	public Queue<List<String>> startRead(String[] inputSplits) {
		logger.info("SqlServer读取数据操作....");
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
		logger.info("SqlServer读取数据结束....");
		return Channel.getQueue();
	}

	@Override
	public String[] createInputSplits() {
		logger.info("SqlServer的Reader开始进行分片开始....");
		String inputSql = String.format("select %s from %s", configBean.getColumn(), configBean.getTable());
		List<String> results = defaultInputSplits(configBean.getColumn(), inputSql);
		logger.info("SqlServer的Reader开始进行分片结束....");
		String[] array = new String[results.size()];
		return results.toArray(array);
	}

	@Override
	public void close() {
		logger.info("SqlServer的Reader开始进行关闭连接开始....");
		DBUtil.close(connection);
		logger.info("SqlServer的Reader开始进行关闭连接结束....");
	}

	@Override
	public int count() {
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement("SELECT count(*) FROM " + configBean.getTable());
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			return resultSet.getInt(1);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			DBUtil.close(preparedStatement);
		}
		return 0;
	}
}
