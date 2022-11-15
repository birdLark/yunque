package www.larkmidtable.com.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * @author stave_zhao
 * @title: DBUtil
 * @projectName honghu
 * @description: 数据库连接工具类
 * @date 2022/11/1509:13
 */
public class DBUtil {

    private static final Logger LOG = LoggerFactory.getLogger(DBUtil.class);


    /**
     * 获取数据库连接
     * @param databaseDriver 驱动类型（根据数据库类型选择）
     * @param jdbcUrl 数据库连接信息
     * @param username 用户名
     * @param password 密码
     * @return
     */
    public static Connection getConnection(String databaseDriver,String jdbcUrl,  String username,  String password) throws SQLException {
        // 连接数据库
        return DriverManager.getConnection(jdbcUrl, username,password);
    }

    /**
     * 关闭连接
     * @param stmt
     * @param conn
     */
    public static void close(PreparedStatement stmt,Connection conn) {
        close(stmt,conn,null);
    }
    /**
     * 关闭连接
     * @param stmt
     * @param conn
     * @param rs
     */
    public static void close(PreparedStatement stmt,Connection conn,ResultSet rs) {
        if(stmt!=null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(conn!=null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(rs!=null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}
