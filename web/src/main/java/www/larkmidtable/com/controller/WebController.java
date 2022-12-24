package www.larkmidtable.com.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 *
 * @Date: 2022/12/24 16:58
 * @Description:
 **/
@RestController
@RequestMapping("/yunque")
public class WebController {

	@GetMapping
	public String getById(){
		System.out.println("YunQue is running");
		return "YunQue getById";
	}
}
