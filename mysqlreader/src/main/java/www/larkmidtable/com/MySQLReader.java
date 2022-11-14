package www.larkmidtable.com;

import www.larkmidtable.com.reader.Reader;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 *
 *
 * @Date: 2022/11/14 11:01
 * @Description:
 **/
public class MySQLReader extends Reader {

	@Override
	public void open() {
		System.out.println("mysql 初始化操作");
	}

	@Override
	public Queue<List<String>> startRead() {
		System.out.println("MySQL读取数据操作....");
		List<String> records =  new ArrayList<>();
		for(int i =0;i<100;i++){
			records.add(i+"条记录");
		}
		getQueue().add(records);
		return getQueue();
	}


	@Override
	public String[] createInputSplits() {
		System.out.println("createInputSplits");
		return new String[0];
	}

	@Override
	public void close() {
		System.out.println("close");
	}
}
