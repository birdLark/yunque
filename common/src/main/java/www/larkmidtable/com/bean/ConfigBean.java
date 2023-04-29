package www.larkmidtable.com.bean;

import lombok.Data;

/**
 *
 *
 * @Date: 2022/11/20 10:19
 * @Description:
 **/
@Data
public class ConfigBean {
	private   String plugin;
	private   String url ;
	private   String username ;
	private   String password ;
	private   String table ;
	private   String column ;
	private   Integer thread;
}
