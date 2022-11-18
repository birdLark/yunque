import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import www.larkmidtable.com.MySQLReader;

import www.larkmidtable.com.MySQLWriter;
import www.larkmidtable.com.channel.Channel;
import www.larkmidtable.com.constant.ReaderPluginEnum;
import www.larkmidtable.com.constant.WriterPluginEnum;
import www.larkmidtable.com.reader.Reader;
import www.larkmidtable.com.reader.oraclereader.OracleReader;
import www.larkmidtable.com.writer.Writer;
import www.larkmidtable.com.writer.oraclewriter.OracleWriter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;
import java.util.Set;


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
		// 1. 使用流关联yml配置文件
		BufferedReader br = null;
		Map<String, String> readerConfig = null;
		Map<String, String> writerConfig = null;
		Reader reader = null;
		Writer writer = null;
		try {
			br = new BufferedReader(new FileReader(HongHuStart.class.getClassLoader()
					.getResource("test.yaml").getPath()));
			Yaml yaml = new Yaml();
			Map<String, Map<String,String>> jobMap = (Map<String, Map<String,String>>) yaml.load(br);
			Set<String> keySet = jobMap.keySet();
			readerConfig = jobMap.get("reader");
			writerConfig = jobMap.get("writer");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String readerPlugin = readerConfig.get("plugin");
		String writerPlugin = writerConfig.get("plugin");
		try {
			reader = getReaderPlugin(readerPlugin);
			writer = getWriterPlugin(writerPlugin);
		} catch (Exception e) {
			logger.error("获取插件失败：",e);
		}

		// 4.Channel
		Channel channel = new Channel();
		channel.channel(reader,writer);
		logger.info("结束迁移任务....");
	}


	private static Writer getWriterPlugin(String name) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		return (Writer) Class.forName(WriterPluginEnum.getByName(name).getClassPath()).newInstance();
	}

	private static Reader getReaderPlugin(String name) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		return (Reader) Class.forName(ReaderPluginEnum.getByName(name).getClassPath()).newInstance();
	}
}
