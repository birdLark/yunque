package www.larkmidtable.com.channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import www.larkmidtable.com.reader.Reader;
import www.larkmidtable.com.writer.Writer;

import java.util.ArrayList;
import java.util.LinkedList;
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

	private static Queue<List<String>> splitQueueByWriterThreadCount(Queue<List<String>> queue, int count){
		Queue<List<String>> resultQueue = new LinkedBlockingQueue<>();
		Queue<String> tempQueue = new LinkedBlockingQueue<>();

		int totalCount = 0;
		for(List<String> list : queue){
			totalCount = totalCount + list.size();
			for(String s : list){
				tempQueue.add(s);
			}
		}
		int batchCount = totalCount / count + 1;

		while (tempQueue.size() != 0){
			List<String> list = new ArrayList<>();
			for(int i = 0; i < batchCount; i ++){
				String poll = tempQueue.poll();
				if(poll == null){
					break;
				}else{
					list.add(poll);
				}
			}
			if(list.size() != 0){
				resultQueue.add(list);
			}
		}

		return resultQueue;
	}

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
			readerCountDownLatch.await();
			queue = splitQueueByWriterThreadCount(queue, writerThread);
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
