import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import www.larkmidtable.com.YunQueWebStart;
import www.larkmidtable.com.dao.TaskDao;

/**
 *
 *
 * @Date: 2022/12/24 18:22
 * @Description:
 **/
// 如果测试类在springBoot启动类的包和子包中，可以省略启动类的设置，也就是省略classes的设定
@SpringBootTest(classes = YunQueWebStart.class)
public class YunQueWebTest {

	@Autowired
	private TaskDao taskDao;


	@Test
	public void test() {
		System.out.println(taskDao.selectById(1));
	}
}
