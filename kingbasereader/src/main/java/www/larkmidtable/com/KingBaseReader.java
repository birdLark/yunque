package www.larkmidtable.com;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import www.larkmidtable.com.reader.Reader;
import www.larkmidtable.com.util.DBType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * @projectName: honghu
 * @package: www.larkmidtable.com
 * @className: KingBaseReader
 * @author: qd.liu
 * @description: 人大金仓读组件
 * @date: 2022/11/17 21:09
 * @version: 1.0
 */
public class KingBaseReader extends Reader {
    private Connection connection;
    private PreparedStatement statement;
    private static Logger logger = LoggerFactory.getLogger(KingBaseReader.class);

    @Override
    public void open() {
        try {
            logger.info("KingBase的Reader建立连接开始....");
            Class.forName(DBType.DM.getDriverClass());
            connection = DriverManager
                    .getConnection("jdbc:kingbase8://127.0.0.1:54321/test", "system", "123456");
            logger.info("KingBase的Reader建立连接结束....");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Queue<List<String>> startRead(String[] inputSplits) {
        logger.info("KingBase读取数据操作....");
        try {
            List<String> records = new ArrayList<>();
            String sql = "select * from student";
            statement = connection.prepareCall(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                records.add(name);
            }
            getQueue().add(records);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("KingBase读取数据结束....");
        return getQueue();
    }


    @Override
    public String[] createInputSplits() {
        logger.info("KingBase的Reader开始进行分片开始....");
        logger.info("Kingbase的Reader开始进行分片结束....");
        return new String[5];
    }

    @Override
    public void close() {
        try {
            logger.info("KingBase的Reader开始进行关闭连接开始....");
            statement.close();
            connection.close();
            logger.info("KingBase的Reader开始进行关闭连接结束....");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
