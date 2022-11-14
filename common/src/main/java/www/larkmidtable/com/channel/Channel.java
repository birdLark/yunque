package www.larkmidtable.com.channel;

import www.larkmidtable.com.reader.Reader;
import www.larkmidtable.com.writer.Writer;

import java.util.List;
import java.util.Queue;

/**
 *
 *
 * @Date: 2022/11/14 18:18
 * @Description:
 **/
public  class Channel {
	//
	public  void channel(Reader reader, Writer writer){
		// 读数据
		reader.open();
		// 切分SQL
		String[] inputSplits = reader.createInputSplits();
		// 读取数据
		Queue<List<String>> records = reader.startRead(inputSplits);
		writer.startWrite(records);

	}
}
