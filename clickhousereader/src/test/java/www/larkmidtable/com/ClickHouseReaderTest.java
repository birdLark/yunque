package www.larkmidtable.com;

import org.junit.Before;
import org.junit.Test;
import www.larkmidtable.com.bean.ConfigBean;

public class ClickHouseReaderTest {
    private ClickHouseReader clickHouseReader = new ClickHouseReader();

    @Before
    public void init() {
        ConfigBean configBean = new ConfigBean();
//        url: "jdbc:mysql://localhost:3306/filedb?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC"
//        username: "root"
//        password: "root"
//        table: "student"
//        column: "*"

        configBean.setUrl("jdbc:clickhouse://192.168.178.128:8123/default");
        configBean.setUsername("default");
        configBean.setPassword("www.2022");
        configBean.setTable("trips");
        configBean.setColumn("trip_id");
        clickHouseReader.setConfigBean(configBean);
    }

    @Test
    public void testReader() {
        clickHouseReader.open();
        String[] inputSplits = clickHouseReader.createInputSplits();
        clickHouseReader.startRead(inputSplits);
        clickHouseReader.close();
    }
}
