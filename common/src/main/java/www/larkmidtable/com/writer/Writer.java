package www.larkmidtable.com.writer;

import java.util.List;
import java.util.Queue;

/**
 *
 *
 * @Date: 2022/11/14 11:03
 * @Description:
 **/
public abstract class Writer {
	// 初始化操作
	public abstract void open();

	// 开始写操作
	public abstract void startWrite(Queue<List<String>> queue);

	// 关闭操作
	public abstract void close() ;
}
