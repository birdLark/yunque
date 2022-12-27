package www.larkmidtable.com.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import www.larkmidtable.com.dao.TaskDao;
import www.larkmidtable.com.domain.Task;
import www.larkmidtable.com.service.ITaskService;

/**
 *
 *
 * @Date: 2022/12/28 0:41
 * @Description:
 **/
@Service
public class TaskServiceImpl extends ServiceImpl<TaskDao, Task> implements ITaskService {

	@Autowired
	private TaskDao taskDao;

	@Override
	public IPage<Task> getPage(int currentPage, int pageSize) {
		IPage page = new Page(currentPage,pageSize);
		taskDao.selectPage(page,null);
		return page;
	}
}
