package www.larkmidtable.com;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import www.larkmidtable.com.channel.Channel;
import www.larkmidtable.com.reader.AbstractDBReader;
import www.larkmidtable.com.util.DBType;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

/**
 * @Date: 2022/11/14 11:01
 * @Description:
 **/
public class MySQLReader extends AbstractDBReader {
    ForkJoinPool forkJoinPool;
    private Connection connection;
    // private PreparedStatement statement;
    private static Logger logger = LoggerFactory.getLogger(MySQLReader.class);


    @Override
    public void open() {
        forkJoinPool = new ForkJoinPool(Math.max(32, Runtime.getRuntime().availableProcessors()));
        try {
            logger.info("MySQL的Reader建立连接开始....");
            Class.forName(DBType.MySql.getDriverClass());
            connection = DriverManager
                    .getConnection(configBean.getUrl(), configBean.getUsername(), configBean.getPassword());
            logger.info("MySQL的Reader建立连接结束....");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public Queue<List<String>> startRead(String[] inputSplits) {
//        logger.info("MySQL读取数据操作....");
//        try {
//            if (inputSplits.length > 1) {
//                // 开启多线程肚
//                batchStartRead(connection, inputSplits);
//            } else {
//                defaultSingleStartRead(connection, inputSplits[0]);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        logger.info("MySQL读取数据结束....");
//        return Channel.getQueue();
//    }
    @Override
    public Queue<List<String>> startRead(String[] inputSplits) {
        logger.info("MySQL读取数据操作....");
        try {
            if(inputSplits.length > 0) {
                List<MySQLReaderParams> params = new ArrayList<>();
                for (String inputSplit : inputSplits) {
                    params.add(new MySQLReaderParams(connection,inputSplit));
                }
                MySQLReaderTask mySQLReaderTask = new MySQLReaderTask();
                mySQLReaderTask.setSingleThreadSize(1);
                mySQLReaderTask.setTaskParams(params);
                ForkJoinTask<List<String>> submit = forkJoinPool.submit(mySQLReaderTask);
                submit.get();//阻塞，内部已经存队列
                return Channel.getQueue();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("MySQL读取数据结束....");
        return Channel.getQueue();
    }


    @Override
    public String[] createInputSplits() {
        logger.info("MySQL的Reader开始进行分片开始....");
        String inputSql = String.format("select %s from %s",configBean.getColumn(), configBean.getTable());
        List<String> results = defaultInputSplits(configBean.getColumn(),inputSql);
        logger.info("MySQL的Reader开始进行分片结束....");
        String[] array = new String[results.size()];
        return results.toArray(array);
    }

    @Override
    public void close() {
        try {
            logger.info("MySQL的Reader开始进行关闭连接开始....");
            connection.close();
            logger.info("MySQL的Reader开始进行关闭连接结束....");
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
