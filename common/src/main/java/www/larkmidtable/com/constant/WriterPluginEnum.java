package www.larkmidtable.com.constant;

import java.util.stream.Stream;

public enum WriterPluginEnum {
    MYSQLWRITER("mysqlwriter","www.larkmidtable.com.MySQLWriter"),
    ORACLEWRITER("oraclewriter","ww.larkmidtable.com.writer.oraclewriter.OracleWriter");
    private String name;
    private String classPath;

    WriterPluginEnum(String name, String classPath) {
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

    public static WriterPluginEnum getByName(String name){
        return Stream.of(WriterPluginEnum.values())
                .filter(pluginEnum -> pluginEnum.name.equals(name))
                .findAny()
                .orElse(null);
    }

}
