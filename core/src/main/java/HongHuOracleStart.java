import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import www.larkmidtable.com.MySQLReader;

import www.larkmidtable.com.MySQLWriter;
import www.larkmidtable.com.channel.Channel;
import www.larkmidtable.com.reader.Reader;
import www.larkmidtable.com.writer.Writer;


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

		logger.info("迁移程序，正式启动中....");
		// 1.解析传递的参数
		Options options = new Options();
		options.addOption("job", true, "作业配置");
		BasicParser parser = new BasicParser();
		CommandLine cl = parser.parse(options, args);
		String jobName = cl.getOptionValue("job");
		logger.info("传递的参数:{} ", jobName);
		// 2.创建Reader
		Reader reader = new MySQLReader();
		// 3.创建Writer
		Writer writer = new MySQLWriter();
		// 4.Channel
		Channel channel = new Channel();
		channel.channel(reader,writer);
		logger.info("结束迁移任务....");
	}
}
