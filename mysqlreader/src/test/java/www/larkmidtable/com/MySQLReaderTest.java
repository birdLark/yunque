package www.larkmidtable.com;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import www.larkmidtable.com.bean.ConfigBean;

import java.sql.*;

public class MySQLReaderTest {
    private MySQLReader mySQLReader = new MySQLReader();
    private Connection conn = null;
    private Statement stmt = null;

    @Before
    public void before() throws SQLException {
        conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "sa");
        stmt = conn.createStatement();
    }

    @Test
    public void shouldTestH2() throws SQLException {
        try{
            stmt.execute("DROP TABLE MY_USER"); //删除已经存在的表
        }catch (Exception e){
            e.printStackTrace();
        }
        stmt.execute("CREATE TABLE MY_USER(ID VARCHAR(10) PRIMARY KEY,NAME VARCHAR(50))"); //创建表
        stmt.executeUpdate("INSERT INTO MY_USER VALUES('001','刘备')"); //插入数据
        stmt.executeUpdate("INSERT INTO MY_USER VALUES('002','关羽')");
        stmt.executeUpdate("INSERT INTO MY_USER VALUES('003','张飞')");

        ResultSet rs = stmt.executeQuery("SELECT NAME FROM MY_USER WHERE ID='001'"); //查询
        while(rs.next()){
            String name = rs.getString("NAME");
            Assert.assertTrue(name.equals("刘备"));
        }
        rs.close();
        stmt.close();
        conn.close();
    }

    @Before
    public void init() {
        ConfigBean configBean = new ConfigBean();
//        url: "jdbc:mysql://localhost:3306/filedb?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC"
//        username: "root"
//        password: "root"
//        table: "student"
//        column: "*"

        configBean.setUrl("jdbc:mysql://localhost:3306/filedb?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC");
        configBean.setUsername("root");
        configBean.setPassword("root");
        configBean.setTable("student");
        configBean.setColumn("id");
        mySQLReader.setConfigBean(configBean);
    }

    @Test
    public void testReader() {
        mySQLReader.open();
        String[] inputSplits = mySQLReader.createInputSplits();
        mySQLReader.startRead(inputSplits);
        mySQLReader.close();
    }
}
