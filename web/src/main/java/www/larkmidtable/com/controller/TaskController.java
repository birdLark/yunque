package www.larkmidtable.com.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import www.larkmidtable.com.domain.Task;
import www.larkmidtable.com.service.ITaskService;
import www.larkmidtable.com.util.R;

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
	public R getAll() {
		return new R(true,taskService.list());
	}


	@PostMapping
	public R save(@RequestBody Task task) {
		return new R(true,taskService.save(task));
	}

	@PutMapping
	public R update(@RequestBody Task task) {
		return new R(true,taskService.updateById(task));
	}

	@DeleteMapping("{id}")
	public R delete(@PathVariable Integer id) {
		return new R(true,taskService.removeById(id));
	}

	@GetMapping("{id}")
	public R getById(@PathVariable Integer id) {
		return new R(true,taskService.getById(id));
	}

	@GetMapping("{currentPage}/{pageSize}")
	public R getPage(@PathVariable int currentPage, @PathVariable int pageSize) {
		return new R(true,taskService.getPage(currentPage, pageSize));
	}

}
