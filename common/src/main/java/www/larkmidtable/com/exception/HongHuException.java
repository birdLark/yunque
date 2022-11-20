package www.larkmidtable.com.exception;

/**
 *
 *
 * @Date: 2022/11/18 23:07
 * @Description:
 **/
public class HongHuException extends RuntimeException  {

	public HongHuException(String message) {
		super(message);
	}

	public HongHuException(String message, Throwable cause) {
		super(message, cause);
	}
}
