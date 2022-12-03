package com.larkmidtable.honghu;

import ch.qos.logback.core.FileAppender;
import com.alibaba.fastjson.JSON;
import com.larkmidtable.honghu.config.ConfigConstant;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import www.larkmidtable.com.bean.ConfigBean;
import www.larkmidtable.com.channel.Channel;
import www.larkmidtable.com.channel.DefaultChannel;
import www.larkmidtable.com.channel.KafkaChannel;
import www.larkmidtable.com.exception.HongHuException;
import www.larkmidtable.com.log.HuFileAppender;
import www.larkmidtable.com.log.HuLogger;
import www.larkmidtable.com.reader.Reader;
import www.larkmidtable.com.writer.Writer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Date;
import java.util.Map;


/**
 *
 *
 * @Date: 2022/11/10 14:28
 * @Description:
 **/
public class HongHuStart {
	private static Logger logger = LoggerFactory.getLogger(HongHuStart.class);

	public static void main(String[] args) throws ParseException {

		logger.info("迁移程序，正式启动中....");
		logger.info("解析传递的参数....");

		Options options = new Options();
		options.addOption("job", true, "作业配置");
		options.addOption("jobId", true, "作业id");

		BasicParser parser = new BasicParser();
		CommandLine cl = parser.parse(options, args);
		String jobName = cl.getOptionValue("job");
		// 如果用户没有明确指定jobid, jobId 默认值为-1
		String jobIdString = cl.getOptionValue("jobId");
		long jobId=-1;
		if (jobIdString!=null && !"-1".equalsIgnoreCase(jobIdString)) {
			jobId = Long.parseLong(jobIdString);
		}
		logger.info("传递的参数:{} ", jobName);
		logger.info("读取作业配置文件....");
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(HongHuStart.class.
					getClassLoader().getResource("test.yaml").getPath()));
		} catch (FileNotFoundException e) {
			throw new HongHuException("文件获取不到", e);
		}
		Yaml yaml = new Yaml();
		Map<String, Map<String, String>> jobMap = //
				(Map<String, Map<String, String>>) yaml.load(br);

		logger.info("解析配置文件....");
		//日志初始化
		Map<String, String> jobConfig = jobMap.get(ConfigConstant.JOB);
		HuFileAppender.initLogPath(jobConfig.get(ConfigConstant.LOGPATH));
		//todo logId 参数获取
		String logFileName = HuFileAppender.makeLogFileName(new Date(), jobId);
		HuFileAppender.contextHolder.set(logFileName);


		Map<String, String> readerConfig = jobMap.get(ConfigConstant.READER);
		Map<String, String> writerConfig = jobMap.get(ConfigConstant.WRITER);
		ConfigBean readerConfigBean = JSON.parseObject(JSON.toJSONString(readerConfig), ConfigBean.class);
		ConfigBean writerConfigBean = JSON.parseObject(JSON.toJSONString(writerConfig), ConfigBean.class);

		String readerPlugin = readerConfig.get(ConfigConstant.READER_PLUGIN);
		String writerPlugin = writerConfig.get(ConfigConstant.WRITER_PLUGIN);

		logger.info("获取Reader和Writer....");
		HuLogger.log("获取Reader和Writer....");
		Reader reader = Reader.getReaderPlugin(readerPlugin,readerConfigBean);
		Writer writer = Writer.getWriterPlugin(writerPlugin,writerConfigBean);

		logger.info("进行读写任务....");
		HuLogger.log("进行读写任务....");
		//通过new KafkaChannel 切换队列
		/*Map<String, String> kafkaConfig = jobMap.get(ConfigConstant.KAFKA);
		Channel channel = new KafkaChannel(kafkaConfig.get(ConfigConstant.HOST),kafkaConfig.get(ConfigConstant.TOPIC),kafkaConfig.get(ConfigConstant.CLIENTID),kafkaConfig.get(ConfigConstant.GROUPID));*/
		Channel channel=new DefaultChannel();
		channel.channel(reader, writer);
		logger.info("结束迁移任务....");

	}
}
