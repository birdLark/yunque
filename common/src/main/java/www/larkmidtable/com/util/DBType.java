package www.larkmidtable.com.util;

/**
 *
 *
 * @Date: 2022/11/14 23:07
 * @Description:
 **/
public enum DBType {
	DB2{
		public String getDriverClass(){
			return "com.ibm.db2.jcc.DB2Driver";
		}
	},
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
	MySql8{
		public String getDriverClass(){//枚举对象实现抽象方法
			return "com.mysql.cj.jdbc.Driver";
		}
	},
	Oracle{
		public String getDriverClass(){//枚举对象实现抽象方法
			return "oracle.jdbc.OracleDriver";
		}
	},
	SQLSERVER{
		public String getDriverClass(){//枚举对象实现抽象方法
			return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		}
	},
	;
	public abstract String getDriverClass();//定义抽象方法

}
