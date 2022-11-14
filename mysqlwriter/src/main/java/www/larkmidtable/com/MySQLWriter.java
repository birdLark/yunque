package www.larkmidtable.com;

import www.larkmidtable.com.writer.Writer;

import java.util.List;
import java.util.Queue;

/**
 *
 *
 * @Date: 2022/11/14 11:01
 * @Description:
 **/
public class MySQLWriter extends Writer {



	public static void main(String[] args) {

	}

	@Override
	public void startWrite(Queue<List<String>> queue) {
		List<String> poll = queue.poll();
		for(int i =0;i<poll.size();i++) {
			System.out.println("往mysql写数据："+poll.get(i));
		}
	}
}
