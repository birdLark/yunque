package www.larkmidtable.com.util;

import lombok.Data;

/**
 *
 *
 * @Date: 2022/12/29 21:18
 * @Description:
 **/
@Data
public class R {
	private boolean flag;
	private Object data;

	public R() {
	}

	public R(boolean flag) {
		this.flag = flag;
	}

	public R(boolean flag, Object data) {
		this.flag = flag;
		this.data = data;
	}
}
