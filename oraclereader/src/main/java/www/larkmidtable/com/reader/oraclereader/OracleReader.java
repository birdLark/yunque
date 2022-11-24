package www.larkmidtable.com.reader.oraclereader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import www.larkmidtable.com.channel.Channel;
import www.larkmidtable.com.reader.AbstractDBReader;
import www.larkmidtable.com.reader.Reader;
import www.larkmidtable.com.util.DBType;
import www.larkmidtable.com.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * @author stave_zhao
 * @title: OracleReader
 * @projectName honghu
 * @description: oracle读数据 工具类
 * @date 2022/11/1509:10
 */
public class OracleReader extends AbstractDBReader {
    private Connection connection ;
//    private PreparedStatement statement ;
    private static Logger logger = LoggerFactory.getLogger(OracleReader.class);

    @Override
    public void open() {
        logger.info("Oracle的Reader建立连接开始....");
        try {
            DBUtil.getConnection(DBType.Oracle.getDriverClass(),configBean.getUrl(), configBean.getUsername(), configBean.getPassword());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.info("Oracle的Reader建立连接结束....");
    }

    @Override
    public Queue<List<String>> startRead(String[] inputSplits) {
        logger.info("Oracle读取数据操作....");
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
        logger.info("Oracle读取数据结束....");
        return Channel.getQueue();
    }

    @Override
    public String[] createInputSplits() {
        logger.info("Oracle的Reader开始进行分片开始....");
        String inputSql = String.format("select %s from %s",configBean.getColumn(), configBean.getTable());
        List<String> results = defaultInputSplits(configBean.getColumn(),inputSql);
        logger.info("Oracle的Reader开始进行分片结束....");
        String[] array = new String[results.size()];
        return results.toArray(array);
    }

    @Override
    public void close() {
        logger.info("Oracle的Reader开始进行关闭连接开始....");
        DBUtil.close(connection);
        logger.info("Oracle的Reader开始进行关闭连接结束....");
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
