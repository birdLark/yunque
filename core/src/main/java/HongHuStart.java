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
 * @Date: 2022/11/10 14:28
 * @Description:
 **/
public class HongHuStart {
	// 定义日志对象
	private static Logger logger = LoggerFactory.getLogger(HongHuStart.class);
	// 程序的入口类
	public static void main(String[] args) throws ParseException {

		logger.info("Hello! 鸿鹄!");
		// 1.解析传递的参数
		Options options = new Options();
		options.addOption("job", true, "Job config.");
		options.addOption("mode", true, "Job runtime mode.");
		BasicParser parser = new BasicParser();
		CommandLine cl = parser.parse(options, args);
		String jobName = cl.getOptionValue("job");
		logger.info("传递的参数:{} ",jobName);
		logger.info("开始迁移任务!");
		// 2.线程池多线程提交任务
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
