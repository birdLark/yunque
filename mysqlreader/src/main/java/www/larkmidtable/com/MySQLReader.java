package www.larkmidtable.com;

import www.larkmidtable.com.reader.Reader;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 *
 * @Date: 2022/11/14 11:01
 * @Description:
 **/
public class MySQLReader extends Reader {

	@Override
	public void open() {
		System.out.println("mysql 初始化操作");
	}

	@Override
	public Queue<List<String>> startRead(String[] inputSplits) {
		System.out.println("MySQL读取数据操作....");
		// 2.线程池多线程提交任务
		ExecutorService executor = Executors.newFixedThreadPool(inputSplits.length);
		List<String> records =  new ArrayList<>();
		for (int i = 0; i < inputSplits.length; i++) {
			executor.submit(() -> {
				records.add("任务处理线程ID: " + Thread.currentThread().getId()+"读取记录");
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
		}
		getQueue().add(records);
		return getQueue();
	}


	@Override
	public String[] createInputSplits() {
		System.out.println("createInputSplits");
		return new String[5];
	}

	@Override
	public void close() {
		System.out.println("close");
	}
}
