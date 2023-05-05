package www.larkmidtable.com.util;

public enum ExitCode {
	PARAMEXIT{
		public int getExitCode(){
			return 0;
		}
	};
	public abstract int getExitCode();//定义抽象方法
}
