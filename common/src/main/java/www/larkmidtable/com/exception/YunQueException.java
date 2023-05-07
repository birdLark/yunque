package www.larkmidtable.com.exception;

import www.larkmidtable.com.transformer.TransformerErrorCode;

/**
 *
 *
 * @Date: 2022/11/18 23:07
 * @Description:
 **/
public class YunQueException extends RuntimeException  {

	public YunQueException(String message) {
		super(message);
	}

	public YunQueException(String message, Throwable cause) {
		super(message, cause);
	}

	public YunQueException(ErrorCode errorCode, Throwable cause) {
		super(errorCode.getDescription(), cause);
	}

	public YunQueException(ErrorCode errorCode) {
		super(errorCode.getDescription());
	}

	public YunQueException(ErrorCode errorCode, String errorMessage) {
		super(errorCode.toString() + " - " + errorMessage);

	}


}
