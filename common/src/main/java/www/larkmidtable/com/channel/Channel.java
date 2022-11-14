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
		reader.open();
		Queue<List<String>> records = reader.startRead();
		writer.startWrite(records);

	}
}
