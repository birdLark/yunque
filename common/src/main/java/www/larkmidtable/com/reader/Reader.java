package www.larkmidtable.com.reader;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 *
 * @Date: 2022/11/14 11:03
 * @Description:
 **/
public abstract class Reader {
	private Queue<List<String>> queue = new LinkedBlockingQueue<List<String>>();

	public Queue<List<String>> getQueue() {
		return queue;
	}

	public void setQueue(Queue<List<String>> queue) {
		this.queue = queue;
	}

	// 初始化操作
	public abstract void open();

	// 读取数据操作
	public abstract Queue<List<String>> startRead(String[] inputSplits);

	// SQL的切片划分
	public abstract String[] createInputSplits();

	// 关闭操作
	public abstract void close() throws IOException;


	public void handler() {
		open();
		createInputSplits();
		close();
	}
}
