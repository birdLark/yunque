package www.larkmidtable.com;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 *
 * @Date: 2022/11/10 14:28
 * @Description:
 **/
public class HongHuStart {
	// 定义日志对象
	private static Logger logger = LoggerFactory.getLogger(HongHuStart.class);
	// 程序的入口类
	public static void main(String[] args) {

		logger.info("Hello! 鸿鹄!");
		// 解析传递的参数
		logger.info("开始迁移任务!");
		// 线程池多线程提交任务
		ExecutorService executor = Executors.newFixedThreadPool(5);
		for (int i = 0; i < 10; i++) {
			executor.submit(() -> {
				logger.info("任务处理线程ID: " + Thread.currentThread().getId());
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
		}

		logger.info("结束迁移任务!");

	}
}
