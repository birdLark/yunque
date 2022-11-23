# 1.配置文件的配置

```
writer:
  plugin: "clickhousereader"
  url: "jdbc:clickhouse://127.0.0.1:8123/default"
  username: "default"
  password: "default"
  table: "student"
  column: "id,name"
```

# 2.参数的说明

| 参数名称 | 参数描述     |
| -------- |----------|
| plugin   | 插件的名称    |
| url      | 请求访问的路径  |
| username | 用户名      |
| password | 密码（可以为空） |
| table    | 读取的表     |
| column   | 读取的列     |



# 3.依赖的JAR包的位置

暂无依赖的JAR包
