package www.larkmidtable.com.reader;

import java.util.List;
import java.util.Queue;

import www.larkmidtable.com.bean.ConfigBean;
import www.larkmidtable.com.constant.ReaderPluginEnum;
import www.larkmidtable.com.exception.YunQueException;

/**
 *
 *
 * @Date: 2022/11/14 11:03
 * @Description:
 **/
public abstract class Reader {


	protected ConfigBean configBean;

	public ConfigBean getConfigBean() {
		return configBean;
	}

	public void setConfigBean(ConfigBean configBean) {
		this.configBean = configBean;
	}

	// 初始化操作
	public abstract void open();

	// 读取数据操作
	public abstract Queue<List<String>> startRead(String[] inputSplits);

	public abstract Queue<List<String>> startRead(String inputSplit);
	// SQL的切片划分
	public abstract String[] createInputSplits();

	// 关闭操作
	public abstract void close() ;


	public static Reader getReaderPlugin(String name, ConfigBean readerConfigBean) {
		try {
			Reader reader = (Reader) Class.forName(ReaderPluginEnum.getByName(readerConfigBean.getPlugin()).getClassPath()).newInstance();
			reader.setConfigBean(readerConfigBean);
			return reader;
		} catch (Exception e) {
			throw new YunQueException("文件获取不到", e);
		}

	}
}
