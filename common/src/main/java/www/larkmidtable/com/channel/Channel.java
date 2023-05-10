package www.larkmidtable.com.channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import www.larkmidtable.com.reader.Reader;
import www.larkmidtable.com.writer.Writer;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

/**
 *
 *
 * @Date: 2022/11/14 18:18
 * @Description:
 **/
public  abstract class Channel {
	private static Logger logger = LoggerFactory.getLogger(Channel.class);

	private static  Queue<List<String>> queue = null;

	public static Queue<List<String>> getQueue() {
		return queue;
	}

	public void setQueue(Queue<List<String>> queue) {
		this.queue = queue;
	}

	public  void channel(Reader reader, Writer writer, CountDownLatch readerCountDownLatch,
			ExecutorService readerexecutor, CountDownLatch writerCountDownLatch, ExecutorService writerexecutor)  {
		try {
			// 1.init 初始化
			reader.open();
			writer.open();
			this.queue = new ArrayBlockingQueue<>(20000);

			// 2.多线程并行读取
			Integer readerThread = reader.getConfigBean().getThread();
			String[] inputSplits = reader.createInputSplits();
			for (int i = 0; i < readerThread; i++) {
				final int n = i;//内部类里m不能直接用,所以赋值给n
				readerexecutor.submit(() -> {
					try {
						reader.startRead(inputSplits[n]);
						readerCountDownLatch.countDown();
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			}

			// 3.多线程并行写入
			Integer writerThread = writer.getConfigBean().getThread();
			for (int i = 0; i < writerThread; i++) {
				writerexecutor.submit(() -> {
					try {
						writer.startWrite();
						writerCountDownLatch.countDown();
					} catch (Exception e ) {
						e.printStackTrace();
					}
				});
			}
		}catch (Exception e) {
			e.printStackTrace();
		}

	}
}
