package www.larkmidtable.com.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import www.larkmidtable.com.domain.Task;
import www.larkmidtable.com.service.ITaskService;

import java.util.List;

/**
 *
 *
 * @Date: 2022/12/24 16:58
 * @Description:
 **/
@RestController
@RequestMapping("/task")
public class TaskController {

	@Autowired
	private ITaskService taskService;

	@GetMapping
	public List<Task> getAll(){
		return taskService.list();
	}


	@PostMapping
	public Boolean save(@RequestBody  Task task){
		return taskService.save(task);
	}

	@PutMapping
	public Boolean update(@RequestBody Task task){
		return taskService.updateById(task);
	}

	@DeleteMapping("{id}")
	public Boolean delete(@PathVariable  Integer id ){
		return taskService.removeById(id);
	}

	@GetMapping("{id}")
	public  Task getById(@PathVariable  Integer id){
		return taskService.getById(id);
	}

@GetMapping("{currentPage}/{pageSize}")
	public IPage<Task> getPage(@PathVariable int currentPage,@PathVariable int pageSize){
		return taskService.getPage(currentPage,pageSize);
}



}
