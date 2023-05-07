package www.larkmidtable.com.util;

public enum ExitCode {
	CALLBACKEXIT{
		public int getExitCode(){
			return 0;
		}
	},
	PARAMERRORKEXIT{
		public int getExitCode(){
			return 1;
		}
	};
	public abstract int getExitCode();//定义抽象方法
}
