#### 1.下载云雀源码

```
$ git clone https://gitee.com/LarkMidTable/yunque.git
```

## 2.通过maven打包

```
$ cd  {yunque_source_code_home}
$ mvn clean package -DskipTests
```
打包成功，日志显示如下：
```
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  03:36 min
[INFO] Finished at: 2023-05-15T14:33:42+08:00
[INFO] ------------------------------------------------------------------------
```
## 3.运行

```
$ sh start.sh -j test -i 12 -p mysql2mysql.yaml -f 'YAML'

-j -- job 任务名称 

-i -- jobId 任务ID 

-p -- path 任务文件路径

-f -- fileFormat 文件类型
```

## 4.查看日志

```
$ cat ${baseDir}/logs/start.out
```