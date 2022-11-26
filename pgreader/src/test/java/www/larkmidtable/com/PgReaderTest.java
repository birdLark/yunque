package www.larkmidtable.com;

import com.alibaba.fastjson.JSONObject;
import org.junit.Before;
import org.junit.Test;
import www.larkmidtable.com.bean.ConfigBean;
import www.larkmidtable.com.channel.Channel;

import java.util.ArrayList;
import java.util.List;

public class PgReaderTest {
    private PgReader pgReader = new PgReader();

    @Before
    public void init() {
        ConfigBean configBean = new ConfigBean();
        configBean.setUrl("jdbc:postgresql://127.0.0.1:5432/test");
        configBean.setUsername("postgres");
        configBean.setPassword("123321");
        configBean.setTable("test_table");
        configBean.setColumn("name");
        pgReader.setConfigBean(configBean);
    }

    @Test
    public void testReader() {
        pgReader.open();
        String[] inputSplits = pgReader.createInputSplits();
        pgReader.startRead(inputSplits);
        pgReader.close();
        while (!Channel.getQueue().isEmpty()){
            List<String> poll = Channel.getQueue().poll();
            System.out.println(poll);
        }

    }
}
