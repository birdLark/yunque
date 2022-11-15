package www.larkmidtable.com.util;

/**
 *
 *
 * @Date: 2022/11/14 23:07
 * @Description:
 **/
public enum DBType {
	DM{
		public String getDriverClass(){//枚举对象实现抽象方法
			return "dm.jdbc.driver.DmDriver";
		}
	},

	MySql{
		public String getDriverClass(){//枚举对象实现抽象方法
			return "com.mysql.jdbc.Driver";
		}
	},
	Oracle{
		public String getDriverClass(){//枚举对象实现抽象方法
			return "oracle.jdbc.OracleDriver";
		}
	};
	public abstract String getDriverClass();//定义抽象方法

}
