package com.larkmidtable.yunque;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.larkmidtable.yunque.config.ConfigConstant;
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
import www.larkmidtable.com.exception.HongHuException;
import www.larkmidtable.com.log.HuFileAppender;
import www.larkmidtable.com.log.HuLogger;
import www.larkmidtable.com.reader.Reader;
import www.larkmidtable.com.transformer.TransformerExecution;
import www.larkmidtable.com.transformer.TransformerInfo;
import www.larkmidtable.com.util.ExitCode;
import www.larkmidtable.com.util.JVMUtil;
import www.larkmidtable.com.util.TransformerUtil;
import www.larkmidtable.com.writer.Writer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 传递的配置文件为JSON
 * @Date: 2022/11/10 14:28
 * @Description:
 **/
public class YunQueJSONStart {
	private static Logger logger = LoggerFactory.getLogger(YunQueJSONStart.class);

	public static void main(String[] args) throws ParseException {
		args = new String[]{"-job", "test", "-jobId", "1", "-jsonPath", "D:\\develop-2023\\yunque-test\\bin\\test.json"};

		logger.info("迁移程序，正式启动中....");

        logger.info("核查参数的正确性....");
        if(args.length == 0 ){
        	logger.info("程序尚未传递参数，需要传递参数如下:");
			logger.error("例如: "+"\n"+" -job 名称 -jobId 自定作业ID -jsonPath \"conf目录下的 mysql2tmysql.json 的全路径!!!\""+"\n"
					+" -job testyunque -jobId testid -jsonPath\"d:....mysql2tmysql.json\""+"\n");
			System.exit(ExitCode.PARAMEXIT.getExitCode());
        }
		logger.info("核查参数的完成....");

		logger.info("解析传递的参数....");
		Options options = new Options();
		options.addOption("job", true, "作业配置");
		options.addOption("jobId", true, "作业id");
		options.addOption("jsonPath", true, "json的路径");

		BasicParser parser = new BasicParser();
		CommandLine cl = parser.parse(options, args);
		String jobName = cl.getOptionValue("job");
		String jobIdString = cl.getOptionValue("jobId");
		String jsonPath = cl.getOptionValue("jsonPath");
		long jobId=-1;
		if (jobIdString!=null && !"-1".equalsIgnoreCase(jobIdString)) {
			jobId = Long.parseLong(jobIdString);
		}
		logger.info("作业名称{} ,作业ID{} ,作业的路径{}", jobName , jobId , jsonPath);
		logger.info("读取作业配置文件....");
		BufferedReader br = null;
		StringBuffer jsonBuffer =new StringBuffer();
		try {
			br = new BufferedReader(new FileReader(jsonPath));
			String contentLine = br.readLine();
			while (contentLine != null) {
				jsonBuffer.append(contentLine);
				contentLine = br.readLine();
			}
		} catch (FileNotFoundException e) {
			throw new HongHuException("文件获取不到", e);
		} catch (NullPointerException e) {
			throw new HongHuException("需要传递参数yamlPath，详情见用户手册", e);
		} catch (IOException e) {
			throw new HongHuException("读取json文件出错，详情见用户手册", e);
		}
		Map<String, Map<String, String>> jobMap = JSON.parseObject (jsonBuffer.toString().trim(),Map.class);
		logger.info("解析配置文件....");
		//日志初始化
		Map<String, String> jobConfig = jobMap.get(ConfigConstant.LOG);
		HuFileAppender.initLogPath(jobConfig.get(ConfigConstant.LOGPATH));
		//todo logId 参数获取
		String logFileName = HuFileAppender.makeLogFileName(new Date(), jobId);
		HuFileAppender.contextHolder.set(logFileName);
		logger.info("加载Transformer插件....");
		List<TransformerExecution> transformerExecutionList=null;
		if(jobMap.get(ConfigConstant.TRANSFORMER)!=null){
			List<TransformerInfo> transformerInfos= JSONArray.parseArray(JSONArray.toJSONString(jobMap.get(ConfigConstant.TRANSFORMER)),TransformerInfo.class);
			 transformerExecutionList = TransformerUtil.buildTransformerInfo(transformerInfos);
		}
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

 		logger.info("创建读写的线程池和计数器...");
		CountDownLatch readerCountDownLatch = new CountDownLatch(readerConfigBean.getThread());
		ExecutorService readerexecutor = Executors.newFixedThreadPool(readerConfigBean.getThread());
		CountDownLatch writerCountDownLatch = new CountDownLatch(writerConfigBean.getThread());
		ExecutorService writerexecutor = Executors.newFixedThreadPool(writerConfigBean.getThread());

		logger.info("进行读写任务....");
		//通过new KafkaChannel 切换队列
		/*Map<String, String> kafkaConfig = jobMap.get(ConfigConstant.KAFKA);
		Channel channel = new KafkaChannel(kafkaConfig.get(ConfigConstant.HOST),kafkaConfig.get(ConfigConstant.TOPIC),kafkaConfig.get(ConfigConstant.CLIENTID),kafkaConfig.get(ConfigConstant.GROUPID));*/
		Channel channel=new DefaultChannel(transformerExecutionList);
		channel.channel(reader, writer,readerCountDownLatch,readerexecutor,
				writerCountDownLatch,writerexecutor);

		// 资源释放
		JVMUtil.shutdownThreadPool(readerexecutor,reader,writer);
		JVMUtil.shutdownThreadPool(writerexecutor,reader,writer);
		try {
			readerCountDownLatch.await();
			writerCountDownLatch.await();
		} catch (InterruptedException e) {
			logger.error("线程等待报错...");
			e.printStackTrace();
		}
		logger.info("结束迁移任务....");
		System.exit(0);

	}
}
