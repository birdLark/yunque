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
 *
 *
 * @Date: 2022/11/14 11:01
 * @Description:
 **/
public class PgReader extends AbstractDBReader {

	private Connection connection ;
	private PreparedStatement statement ;
	private static Logger logger = LoggerFactory.getLogger(PgReader.class);
	@Override
	public void open() {
		try {
			logger.info("PostgreSQL的Reader建立连接开始....");
			Class.forName(DBType.PostgreSql.getDriverClass());
			connection = DriverManager
					.getConnection(configBean.getUrl(), configBean.getUsername(), configBean.getPassword());
//					.getConnection("jdbc:postgresql://127.0.0.1:5432/test","postgres","123321");
			logger.info("PostgreSQL的Reader建立连接结束....");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Queue<List<String>> startRead(String[] inputSplits) {
		logger.info("PostgreSQL读取数据操作....");
		try {
			if (inputSplits.length > 1) {
				// 开启多线程肚
				batchStartRead(connection, inputSplits);
			} else {
				defaultSingleStartRead(connection, inputSplits[0]);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		logger.info("PostgreSQL读取数据结束....");
		return Channel.getQueue();
	}

	@Override
	public Queue<List<String>> startRead(String inputSplit) {
		return null;
	}


	@Override
	public String[] createInputSplits() {
		logger.info("PostgreSQL的Reader开始进行分片开始....");
		String inputSql = String.format("select %s from %s",configBean.getColumn(), configBean.getTable());
		List<String> results = pgDefaultInputSplits(configBean.getColumn(),inputSql);
		logger.info("PostgreSQL的Reader开始进行分片结束....");
		String[] array = new String[results.size()];
		return results.toArray(array);
	}

	/**
	 * pg分页方法
	 * @param column
	 * @param originInput
	 * @return
	 */
	public List<String> pgDefaultInputSplits(String column,String originInput) {
		int pageSize = 5;
		List<String> splits = new ArrayList<>();
		int count = count();
		if (count > 0 && 1 == 1) {// 1==1 后续可开启切分SQL配置参数
			// 拆分的大小
			int size = count / pageSize;
			int lastCount = count % pageSize;
			if (lastCount > 0) {
				size = size + 1;
			}
			for (int i = 0; i < size; i++) {
				StringBuilder builder = new StringBuilder("SELECT "+column+" FROM ( ");
				builder.append(" ").append(originInput).append(" ) t").append(" ").append("LIMIT");
				int limitStart = i * pageSize;
				int j = i + 1;
				//最后一页
				if (j == size && lastCount!=0) {
					builder.append(" ").append(lastCount).append(" OFFSET ").append(limitStart);
				} else {
					builder.append(" ").append(pageSize).append(" OFFSET ").append(limitStart);
				}
				splits.add(builder.toString());
			}

		} else {
			splits.add(originInput);
		}
		return splits;
	}

	@Override
	public void close()  {
		try {
			logger.info("PostgreSQL的Reader开始进行关闭连接开始....");
			connection.close();
			logger.info("PostgreSQL的Reader开始进行关闭连接结束....");
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
}
