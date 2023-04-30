package www.larkmidtable.com;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import www.larkmidtable.com.channel.Channel;
import www.larkmidtable.com.reader.AbstractDBReader;
import www.larkmidtable.com.util.DBType;

import java.sql.*;
import java.util.List;
import java.util.Queue;

/**
 * @Date: 2022/11/14 11:01
 * @Description:
 **/
public class ClickHouseReader extends AbstractDBReader {

    private Connection connection;
    // private PreparedStatement statement;
    private static Logger logger = LoggerFactory.getLogger(ClickHouseReader.class);


    @Override
    public void open() {
        try {
            logger.info("ClickHouse的Reader建立连接开始....");
            Class.forName(DBType.ClickHouse.getDriverClass());
            connection = DriverManager
                    .getConnection(configBean.getUrl(), configBean.getUsername(), configBean.getPassword());
            logger.info("ClickHouse的Reader建立连接结束....");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Queue<List<String>> startRead(String[] inputSplits) {
        logger.info("ClickHouse读取数据操作....");
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
        logger.info("ClickHouse读取数据结束....");
        return Channel.getQueue();
    }

	@Override
	public Queue<List<String>> startRead(String inputSplit) {
		return null;
	}


	@Override
    public String[] createInputSplits() {
        logger.info("ClickHouse的Reader开始进行分片开始....");
        String inputSql = String.format("select %s from %s",configBean.getColumn(), configBean.getTable());
        List<String> results = defaultInputSplits(configBean.getColumn(),inputSql);
        logger.info("ClickHouse的Reader开始进行分片结束....");
        String[] array = new String[results.size()];
        return results.toArray(array);
    }

    @Override
    public void close() {
        try {
            logger.info("ClickHouse的Reader开始进行关闭连接开始....");
            connection.close();
            logger.info("ClickHouse的Reader开始进行关闭连接结束....");
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
