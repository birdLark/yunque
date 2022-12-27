package www.larkmidtable.com.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import www.larkmidtable.com.domain.Task;

public interface ITaskService extends IService<Task> {


	IPage<Task> getPage(int currentPage,int pageSize);
}
