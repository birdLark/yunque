package www.larkmidtable.com.writer.oraclewriter;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import www.larkmidtable.com.channel.Channel;
import www.larkmidtable.com.util.DBType;
import www.larkmidtable.com.util.DBUtil;
import www.larkmidtable.com.writer.Writer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Queue;

/**
 * @author stave_zhao
 * @title: OracleWriter
 * @projectName honghu
 * @description: oracle 写数据工具类
 * @date 2022/11/1509:12
 */
public class OracleWriter extends Writer {
    private Connection connection ;
    private PreparedStatement statement ;
    private static Logger logger = LoggerFactory.getLogger(OracleWriter.class);
    @Override
    public void open() {
        logger.info("Oracle的Writer建立连接开始....");
        try {
            connection = DBUtil.getConnection(DBType.Oracle.getDriverClass(),configBean.getUrl(),configBean.getUsername(),configBean.getPassword());
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.info("Oracle的Writer建立连接结束....");
    }

    @Override
    public void startWrite() {
        logger.info("Oracle开始写数据....");
        List<String> poll = Channel.getQueue().poll();
        String[] columns = configBean.getColumn().split(",");
        StringBuffer sb=new StringBuffer();
        for(int i =0;i<columns.length;i++) {sb.append("?,");}
        String whstr = sb.toString().substring(0, sb.toString().length() - 1);
        String sql = String.format("insert into %s(%s) values (%s)",configBean.getTable(),configBean.getColumn(),whstr);
        try {
            // 批量插入时ps对象必须放到for循环外面
            statement = connection.prepareStatement(sql);
            for (int i = 0; i < poll.size(); i++) {
                JSONObject jsonObject = JSONObject.parseObject(poll.get(i));

                for(int j =1;j<=columns.length;j++) {
                    statement.setObject(j,jsonObject.get(columns[j-1]));
                }
                statement.addBatch();
                if (i % 10000 == 0) {
                    statement.executeBatch();
                    connection.commit();
                    statement.clearBatch();
                }
            }
            statement.executeBatch();
            connection.commit();
            statement.clearBatch();
        }catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("Oracle写数据完成....");
    }

    @Override
    public void close() {
        logger.info("Oracle的Writter开始进行关闭连接开始....");
        DBUtil.close(statement,connection);
        logger.info("Oracle的Writer开始进行关闭连接结束....");
    }
}
