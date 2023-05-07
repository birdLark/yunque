package com.larkmidtable.yunque;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
import www.larkmidtable.com.exception.YunQueException;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 云雀启动类
 * @Date: 2022/11/10 14:28
 * @Description:
 **/
public class YunQueEngine {
	private static Logger logger = LoggerFactory.getLogger(YunQueEngine.class);

	public static void main(String[] args) throws ParseException {
		logger.info("Hello! 欢迎使用云雀数据集成....");

		if(args.length == 0) {
			args = new String[]{"-job",
					"test",
					"-jobId",
					"1",
					"-path",
					"E:\\larksource\\A_open\\ee\\yunque\\conf\\mysql2mysql.yaml",
					"-fileFormat",
					"YAML"};
			logger.warn("尚未传递参数，运行的为默认配置....");
		}

		logger.info("核查参数的正确性....");
		if(args.length != 8 ){
			logger.info("程序尚未传递参数，需要传递参数如下:");
			logger.error("例如: "+"\n"+" -job <名称> -jobId <自定作业ID> -path \"<conf目录下的 mysql2tmysql.json 的全路径!!!>\" -fileFormat <作业文件格式 JSON 或者 YAML>"+"\n"
					+" -job testyunque -jobId testid -path \"D:/yunque/mysql2tmysql.json\" -fileFormat JSON"+"\n");
			throw new YunQueException("运行单体类需要传递参数...");
		}
		logger.info("核查参数的完成....");

		logger.info("解析传递的参数....");
		BasicParser parser = new BasicParser();

		Options options = new Options();
		options.addOption("job", true, "作业配置");
		options.addOption("jobId", true, "作业id");
		options.addOption("path", true, "作业文件路径");
		options.addOption("fileFormat", true, "作业文件格式 JSON或者YAML");
		CommandLine cl = parser.parse(options, args);
		String jobName = cl.getOptionValue("job");
		String jobIdString = cl.getOptionValue("jobId");
		String path = cl.getOptionValue("path");
		String fileFormat = cl.getOptionValue("fileFormat");
		long jobId=-1;
		if (jobIdString!=null && !"-1".equalsIgnoreCase(jobIdString)) {
			jobId = Long.parseLong(jobIdString);
		}
		logger.info("作业名称{} ,作业ID{} ,作业的路径{} ,作业文件的格式{}", jobName , jobId , path ,fileFormat);
		logger.info("读取作业配置文件....");
		BufferedReader br = null;
		StringBuffer jsonBuffer =new StringBuffer();
		try {
			br = new BufferedReader(new FileReader(path));
		} catch (FileNotFoundException e) {
			throw new YunQueException("作业文件路径获取不到，核查参数path的配置....", e);
		}
		Map<String, Map<String, String>> jobMap = new HashMap<String, Map<String, String>>();
		if("JSON".equals(fileFormat)) {
			try {
				String contentLine = br.readLine();
				while (contentLine != null) {
					jsonBuffer.append(contentLine);
					contentLine = br.readLine();
				}
			} catch (IOException e) {
				throw new YunQueException("作业文件配置出错，详情见用户手册配置....", e);
			}
			jobMap = JSON.parseObject (jsonBuffer.toString().trim(),Map.class);
		} else if("YAML".equals(fileFormat)) {
			Yaml yaml = new Yaml();
			jobMap = (Map<String, Map<String, String>>) yaml.load(br);
		} else {
			throw new YunQueException("运行模式参数 -fileFormat 取值 <JSON 或者 YAML>....");
		}
		logger.info("解析配置文件....");
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
			logger.error("线程等待报错....");
			e.printStackTrace();
		}
		logger.info("结束迁移任务....");
		System.exit(ExitCode.CALLBACKEXIT.getExitCode());
	}
}
