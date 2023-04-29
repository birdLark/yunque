import com.larkmidtable.yunque.YunQueYAMLStart;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import www.larkmidtable.com.util.JVMUtil;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 *
 * @Date: 2022/11/14 14:22
 * @Description:
 **/
public class ThreadTest {
	// 定义日志对象
	private static Logger logger = LoggerFactory.getLogger(YunQueYAMLStart.class);

	public static void main(String[] args) throws ParseException, InterruptedException {

		logger.info("Hello! 云雀!");

		logger.info("开始迁移任务!");

		// 2.线程池多线程提交任务
		ExecutorService executor = Executors.newFixedThreadPool(10);
		CountDownLatch countDownLatch = new CountDownLatch(10);
		for (int i = 0; i < 10; i++) {
			executor.submit(() -> {
				logger.info("任务处理线程ID: " + Thread.currentThread().getId()+"读取记录");
				try {
					Thread.sleep(1000L);
					countDownLatch.countDown();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
		}
		//
//		JVMUtil.shutdownThreadPool(executor);
		// 主线程在阻塞，当计数器==0，就唤醒主线程往下执行。
		countDownLatch.await();
		System.exit(0);
	}

}
