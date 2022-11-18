# 1.配置文件的配置

```
writer:
  plugin: "db2reader"
  url: "jdbc:db2://192.168.2.8:50000/sample"
  username: "db2admin"
  password: "db2admin"
  table: "GOOCH.ACT"
  column: "ACTNO,ACTKWD"
```

# 2.参数的说明

| 参数名称 | 参数描述       |
| -------- | -------------- |
| plugin   | 插件的名称     |
| url      | 请求访问的路径 |
| username | 用户名         |
| password | 密码           |
| table    | 读取的表       |
| column   | 读取的列       |



# 3.依赖的JAR包的位置

暂无依赖的JAR包
