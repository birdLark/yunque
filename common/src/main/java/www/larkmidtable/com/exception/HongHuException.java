package www.larkmidtable.com.exception;

import www.larkmidtable.com.transformer.TransformerErrorCode;

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

	public HongHuException(ErrorCode errorCode, Throwable cause) {
		super(errorCode.getDescription(), cause);
	}

	public HongHuException(ErrorCode errorCode) {
		super(errorCode.getDescription());
	}

	public HongHuException(ErrorCode errorCode, String errorMessage) {
		super(errorCode.toString() + " - " + errorMessage);
	}


}
