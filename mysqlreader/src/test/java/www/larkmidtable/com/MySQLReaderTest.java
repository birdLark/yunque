package www.larkmidtable.com;

import org.junit.Before;
import org.junit.Test;
import www.larkmidtable.com.bean.ConfigBean;

public class MySQLReaderTest {
    private MySQLReader mySQLReader = new MySQLReader();

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
