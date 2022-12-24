package www.larkmidtable.com.controller;

import org.springframework.web.bind.annotation.*;

/**
 *
 *
 * @Date: 2022/12/24 16:58
 * @Description:
 **/
@RestController
@RequestMapping("/yunque")
public class WebController {

	@PostMapping
	public String save(){
		return "yunque add";
	}

	@DeleteMapping("/{id}")
	public String delete(){
		return "yunque delete";
	}

	@PutMapping
	public String update(){
		return "yunque update";
	}

	@GetMapping("/{id}")
	public  String getById(){
		return "yunque getById";
	}

	@GetMapping
	public String getAll(){
		return "yunque getAll";
	}

}
