package www.larkmidtable.com.channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import www.larkmidtable.com.reader.Reader;
import www.larkmidtable.com.writer.Writer;

import java.io.IOException;
import java.util.List;
import java.util.Queue;

/**
 *
 *
 * @Date: 2022/11/14 18:18
 * @Description:
 **/
public  class Channel {


	public  void channel(Reader reader, Writer writer)  {
		try {
			reader.open();
			writer.open();
			String[] inputSplits = reader.createInputSplits();
			Queue<List<String>> records = reader.startRead(inputSplits);
			writer.startWrite(records);
			reader.close();
			writer.close();
		}catch (Exception e) {
			e.printStackTrace();
		}

	}
}
