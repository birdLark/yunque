package www.larkmidtable.com.model;

import lombok.Data;

import java.io.Serializable;
/**
 *  log result
 * @Author: lzq
 * @Date: 2022/12/2 19:33
 */
@Data
public class LogResult implements Serializable {
    private static final long serialVersionUID = 42L;

    public LogResult(int fromLineNum, int toLineNum, String logContent, boolean isEnd) {
        this.fromLineNum = fromLineNum;
        this.toLineNum = toLineNum;
        this.logContent = logContent;
        this.isEnd = isEnd;
    }

    private int fromLineNum;
    private int toLineNum;
    private String logContent;
    private boolean isEnd;


}
