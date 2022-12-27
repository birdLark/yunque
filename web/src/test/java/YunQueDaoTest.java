import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import www.larkmidtable.com.YunQueWebStart;
import www.larkmidtable.com.dao.TaskDao;
import www.larkmidtable.com.domain.Task;

import java.util.List;

/**
 *
 *
 * @Date: 2022/12/24 18:22
 * @Description:
 **/
// 如果测试类在springBoot启动类的包和子包中，可以省略启动类的设置，也就是省略classes的设定
@SpringBootTest(classes = YunQueWebStart.class)
public class YunQueDaoTest {

	@Autowired
	private TaskDao taskDao;


	@Test
	public void testSelectById() {
		taskDao.selectById(1);
	}


	@Test
	public void testGetPage() {
		IPage page = new Page(1,5);
		// QueryWrapper 连着条件带分页查询
		taskDao.selectPage(page,null);
		List records = page.getRecords();
		System.out.println(records);
	}

	@Test
	public void testByCondition() {
		String name ="yunque";
		QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().like(name != null,Task::getName,name);
		taskDao.selectList(queryWrapper);
	}



}
