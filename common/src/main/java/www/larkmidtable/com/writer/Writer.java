package www.larkmidtable.com.writer;

import www.larkmidtable.com.bean.ConfigBean;
import www.larkmidtable.com.constant.WriterPluginEnum;
import www.larkmidtable.com.exception.HongHuException;

import java.io.IOException;
import java.util.List;
import java.util.Queue;

/**
 *
 *
 * @Date: 2022/11/14 11:03
 * @Description:
 **/
public abstract class Writer {
	public static Writer getWriterPlugin(String name, ConfigBean readerConfigBean) {
		try {
			return (Writer) Class.forName(WriterPluginEnum.getByName(name).getClassPath()).newInstance();
		} catch (Exception e) {
			throw new HongHuException("文件获取不到", e);
		}
	}

	// 初始化操作
	public abstract void open();

	// 开始写操作
	public abstract void startWrite();

	// 关闭操作
	public abstract void close() throws IOException;
}
