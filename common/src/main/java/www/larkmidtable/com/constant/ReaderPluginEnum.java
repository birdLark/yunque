package www.larkmidtable.com.constant;

import java.util.stream.Stream;

/**
 * 所有插件名称与类路径
 * @author
 */
public enum ReaderPluginEnum {
    MYSQLREADER("mysqlreader","www.larkmidtable.com.MySQLReader"),
    ORACLEREADER("oraclelreader","www.larkmidtable.com.reader.oraclereader.OracleReader");
    private String name;
    private String classPath;

    ReaderPluginEnum(String name, String classPath) {
        this.name = name;
        this.classPath = classPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassPath() {
        return classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

    public static ReaderPluginEnum getByName(String name){
        return Stream.of(ReaderPluginEnum.values())
                .filter(pluginEnum -> pluginEnum.name.equals(name))
                .findAny()
                .orElse(null);
    }
}
