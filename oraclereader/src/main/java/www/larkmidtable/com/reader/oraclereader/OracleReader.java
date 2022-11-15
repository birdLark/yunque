package www.larkmidtable.com.reader.oraclereader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class OracleReader extends Reader {
    private Connection connection ;
    private PreparedStatement statement ;
    private static Logger logger = LoggerFactory.getLogger(OracleReader.class);

    @Override
    public void open() {
        logger.info("Oracle的Reader建立连接开始....");

        try {
            String url="jdbc:oracle:thin:@localhost:1521:orcl";
            String username="root";
            String password="12345678";
            DBUtil.getConnection(DBType.Oracle.getDriverClass(),url,username,password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.info("Oracle的Reader建立连接结束....");
    }

    @Override
    public Queue<List<String>> startRead(String[] inputSplits) {
        logger.info("Oracle读取数据操作....");
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
        logger.info("Oracle读取数据结束....");
        return getQueue();
    }

    @Override
    public String[] createInputSplits() {
        logger.info("Oracle的Reader开始进行分片开始....");
        logger.info("Oracle的Reader开始进行分片结束....");
        return new String[5];
    }

    @Override
    public void close() {
        logger.info("Oracle的Reader开始进行关闭连接开始....");
        DBUtil.close(statement,connection);
        logger.info("Oracle的Reader开始进行关闭连接结束....");
    }
}
