package www.larkmidtable.com;

import org.junit.Before;
import org.junit.Test;
import www.larkmidtable.com.bean.ConfigBean;
import www.larkmidtable.com.channel.Channel;
import www.larkmidtable.com.reader.oraclereader.OracleReader;
import www.larkmidtable.com.reader.oraclereader.OracleWriter;
import www.larkmidtable.com.util.DBType;

import java.util.List;
import java.util.Queue;

/**
 * TODO 类描述
 *
 * @author Wang
 * @date 2022/12/2 13:43
 */
public class OracleReaderTest {
    private OracleReader oracleReader = new OracleReader();
    private OracleWriter oracleWriter = new OracleWriter();

    @Before
    public void init() {
        ConfigBean configBean = new ConfigBean();
        configBean.setUrl("jdbc:oracle:thin:@127.0.0.1:ORCL");
        configBean.setUsername("root");
        configBean.setPassword("test");
        configBean.setTable("COPY");
        configBean.setColumn("id");
        oracleReader.setConfigBean(configBean);

        configBean.setUrl("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC");
        configBean.setUsername("root");
        configBean.setPassword("root");
        configBean.setTable("test");
        configBean.setColumn("name");
        configBean.setPlugin(DBType.MySql.getDriverClass());
        oracleWriter.setConfigBean(configBean);
    }

    @Test
    public void testReader() {
        oracleReader.open();
        String[] inputSplits = oracleReader.createInputSplits();
        System.out.println(oracleReader.count());
        Queue<List<String>> x = oracleReader.startRead(inputSplits);
        oracleReader.close();
        System.out.println(x.element());

    }
}
