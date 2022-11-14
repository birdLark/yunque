import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private static Logger logger = LoggerFactory.getLogger(HongHuStart.class);

	public static void main(String[] args) throws ParseException {

		logger.info("Hello! 鸿鹄!");

		logger.info("开始迁移任务!");

		// 2.线程池多线程提交任务
		//		ExecutorService executor = Executors.newFixedThreadPool(inputSplits.length);

		//		for (int i = 0; i < inputSplits.length; i++) {
		//			executor.submit(() -> {
		//				records.add("任务处理线程ID: " + Thread.currentThread().getId()+"读取记录");
		//				try {
		//					Thread.sleep(1000L);
		//				} catch (InterruptedException e) {
		//					e.printStackTrace();
		//				}
		//			});
		//		}

		logger.info("结束迁移任务!");
	}
}