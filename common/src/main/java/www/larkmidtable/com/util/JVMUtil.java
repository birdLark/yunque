package www.larkmidtable.com.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import www.larkmidtable.com.reader.Reader;
import www.larkmidtable.com.writer.Writer;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 *
 * @Date: 2023/4/29 12:48
 * @Description:
 **/
public class JVMUtil {

	private static Logger logger = LoggerFactory.getLogger(JVMUtil.class);

	public static void shutdownThreadPool(ExecutorService threadPool,Reader reader, Writer writer) {
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				try {
					logger.info("开始销毁线程池...");
					try {
						threadPool.shutdownNow(); // 取消当前执行的任务
						reader.close();
						try {
							writer.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						if (!threadPool.awaitTermination(5, TimeUnit.SECONDS))
							logger.error("销毁线程池失败...");
					} catch (InterruptedException ie) {
						threadPool.shutdownNow();
						logger.error("销毁线程池...");
						Thread.currentThread().interrupt();
						reader.close();
						try {
							writer.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					logger.info("成功销毁线程池....");
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
