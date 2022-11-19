package www.larkmidtable.com.channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import www.larkmidtable.com.reader.Reader;
import www.larkmidtable.com.writer.Writer;

import java.io.IOException;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 *
 * @Date: 2022/11/14 18:18
 * @Description:
 **/
public  class Channel {

	private static Queue<List<String>> queue = new LinkedBlockingQueue<List<String>>();

	public static Queue<List<String>> getQueue() {
		return queue;
	}

	public void setQueue(Queue<List<String>> queue) {
		this.queue = queue;
	}

	public  void channel(Reader reader, Writer writer)  {
		try {
			reader.open();
			writer.open();
			String[] inputSplits = reader.createInputSplits();
			reader.startRead(inputSplits);
			writer.startWrite();
			reader.close();
			writer.close();
		}catch (Exception e) {
			e.printStackTrace();
		}

	}
}
