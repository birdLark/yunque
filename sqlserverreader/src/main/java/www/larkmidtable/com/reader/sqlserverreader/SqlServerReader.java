package www.larkmidtable.com.reader.sqlserverreader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import www.larkmidtable.com.channel.Channel;
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
 * @author fei
 * @description: sqlserver 读工具
 * @date 2022-11-15
 */
public class SqlServerReader extends Reader {
    private Connection connection ;
    private PreparedStatement statement ;
    private static Logger logger = LoggerFactory.getLogger(SqlServerReader.class);

    @Override
    public void open() {
        logger.info("SqlServer的Reader建立连接开始....");

        try {
        	String url="jdbc:sqlserver://localhost:1433;DatabaseName=ILS";
            String username="manh";
            String password="1qaz@WSX";
            DBUtil.getConnection(DBType.SQLSERVER.getDriverClass(),url,username,password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.info("SqlServer的Reader建立连接结束....");
    }

    @Override
    public Queue<List<String>> startRead(String[] inputSplits) {
        logger.info("SqlServer读取数据操作....");
        try {
            List<String> records =  new ArrayList<>();
            String sql = "select * from student";
            statement = connection.prepareCall(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                records.add(name);
            }
			Channel.getQueue().add(records);
        }catch (Exception e){
            e.printStackTrace();
        }
        logger.info("SqlServer读取数据结束....");
        return Channel.getQueue();
    }

    @Override
    public String[] createInputSplits() {
        logger.info("SqlServer的Reader开始进行分片开始....");
        logger.info("SqlServer的Reader开始进行分片结束....");
        return new String[5];
    }

    @Override
    public void close() {
        logger.info("SqlServer的Reader开始进行关闭连接开始....");
        DBUtil.close(statement,connection);
        logger.info("SqlServer的Reader开始进行关闭连接结束....");
    }
}
