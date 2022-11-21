# 1.配置文件的配置

```
writer:
  plugin: "starrockswrite"
  url: "jdbc:mysql://10.28.60.132:9030/example_db?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC"
  username: "root"
  password: "root"
  table: "table1"
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
