package www.larkmidtable.com;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import www.larkmidtable.com.concurrent.TaskParams;

import java.sql.Connection;

/**
 * @author daizhong.liu
 * @description TODO
 * @date 2022/11/24 0024 17:11
 */
@Getter
@Setter
@AllArgsConstructor
public class MySQLReaderParams extends TaskParams {
    private Connection connection;
    private String splitSql;
}
