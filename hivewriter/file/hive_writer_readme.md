# 1.配置文件的配置

```
writer:
  plugin: "Hivewriter"
  url: "jdbc:hive2://node11:10000/didu"
  username: "root"
  password: "root"
  table: "student"
  column: "id,name"
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
