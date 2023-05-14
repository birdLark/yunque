## 1获取代码

```
git clone https://gitee.com/LarkMidTable/yunque.git
```

## 2 编译

```
mvn clean package -DskipTests
```

## 3.运行

```
sh start.sh -j test -i 12 -p mysql2mysql.yaml -f 'YAML'

-j -- job 任务名称 

-i -- jobId 任务ID 

-p -- path 任务文件路径

-f -- fileFormat 文件类型
```

## 4.查看日志

```
${baseDir}/logs/start.out
```