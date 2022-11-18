package com;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import www.larkmidtable.com.channel.Channel;
import www.larkmidtable.com.reader.Reader;
import www.larkmidtable.com.util.DBType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Queue;

/**
 * @Date: 2022/11/14 11:01
 **/
public class DB2Reader extends Reader {

    private Connection connection;
    private PreparedStatement statement;
    private static Logger logger = LoggerFactory.getLogger(DB2Reader.class);

    @Override
    public void open() {
        try {
            logger.info("db2的Reader建立连接开始....");
            Class.forName(DBType.DB2.getDriverClass());
            Properties properties = new Properties();
            properties.setProperty("user", "db2admin");
            properties.setProperty("password", "db2admin");

            connection = DriverManager
                    .getConnection("jdbc:db2://192.168.2.8:50000/sample", properties);
//					.getConnection("jdbc:db2://localhost:50000/sample?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC","db2admin","db2admin");
            logger.info("db2的Reader建立连接结束....");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Queue<List<String>> startRead(String[] inputSplits) {
        logger.info("DB2读取数据操作....");
        try {
            List<String> records = new ArrayList<>();
            String sql = "select * from GOOCHSAMA.ACT";
            statement = connection.prepareCall(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String empno = resultSet.getString("ACTNO");
                String actkwd = resultSet.getString("ACTKWD");
                System.out.println(actkwd);
                records.add(empno);
            }
			Channel.getQueue().add(records);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("DB2读取数据结束....");
        return Channel.getQueue();
    }


    @Override
    public String[] createInputSplits() {
        logger.info("DB2的Reader开始进行分片开始....");
        logger.info("DB2的Reader开始进行分片结束....");
        return new String[5];
    }

    @Override
    public void close() {
        try {
            logger.info("DB2的Reader开始进行关闭连接开始....");
            statement.close();
            connection.close();
            logger.info("DB2的Reader开始进行关闭连接结束....");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
