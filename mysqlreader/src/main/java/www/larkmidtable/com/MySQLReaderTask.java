package www.larkmidtable.com;

import com.alibaba.fastjson.JSONObject;
import www.larkmidtable.com.channel.Channel;
import www.larkmidtable.com.concurrent.ForkJoinRecursiveTask;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @Author daizhong.liu
 * @Date 2022/11/24 0024 17:05
 */
public class MySQLReaderTask extends ForkJoinRecursiveTask<MySQLReaderParams, String> {
    @Override
    protected ForkJoinRecursiveTask<MySQLReaderParams, String> buildTask() {
        return new MySQLReaderTask();
    }

    @Override
    public List<String> doProcess(List<MySQLReaderParams> params) {
        List<String> result = new ArrayList<>();
        params.stream().forEach(param -> {
            Connection connection = param.getConnection();
            String splitSql = param.getSplitSql();
            try {
                CallableStatement callableStatement = connection.prepareCall(splitSql);
                ResultSet resultSet = callableStatement.executeQuery();
                while (resultSet.next()) {
                    Map<String, Object> map = new HashMap<>();
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    for (int i = 1; i <= metaData.getColumnCount(); i++) {
                        map.put(metaData.getColumnName(i), resultSet.getObject(metaData.getColumnName(i)));
                    }
                    result.add(JSONObject.toJSONString(map));
                }
                Channel.getQueue().add(result);
            }catch (Exception e) {
                e.printStackTrace();
            }
        });
        return result;
    }
}
