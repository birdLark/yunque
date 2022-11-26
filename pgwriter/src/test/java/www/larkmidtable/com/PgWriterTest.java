package www.larkmidtable.com;

import com.alibaba.fastjson.JSONObject;
import org.junit.Before;
import org.junit.Test;
import www.larkmidtable.com.bean.ConfigBean;
import www.larkmidtable.com.channel.Channel;

import java.util.ArrayList;

public class PgWriterTest {
    private PgWriter pgWriter = new PgWriter();

    @Before
    public void init() {
        ConfigBean configBean = new ConfigBean();
        configBean.setUrl("jdbc:postgresql://127.0.0.1:5432/test");
        configBean.setUsername("postgres");
        configBean.setPassword("123321");
        configBean.setTable("test_table");
        configBean.setColumn("name,sex,remark");
        pgWriter.setConfigBean(configBean);
    }

    @Test
    public void testWriter() {
        ArrayList<String> strings = new ArrayList<>();
        for(int i=0;i<3;i++){
            JSONObject row = new JSONObject();
            row.put("name","a"+i);
            row.put("sex",i);
            row.put("remark","a"+i);
            strings.add(row.toJSONString());
        }
        Channel.getQueue().add(strings);

        pgWriter.open();
        pgWriter.startWrite();
        pgWriter.close();
    }
}
