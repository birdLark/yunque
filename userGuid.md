# Quick Start

### 1.下载云雀源码

```
$ git clone https://gitee.com/LarkMidTable/yunque.git
```

### 2.通过maven打包

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
打包成功后的tar.gz包位于 {yunque_source_code_home}/yunque-1.0-SNAPSHOT-release.tar.gz,如下：
```
$ ls {yunque_source_code_home}
assembly          core       docs                 hdfswriter   kingbasereader  oraclewriter  sqlserverreader  web
bin               db2reader  dorisread            hivereader   kingbasewriter  pgreader      sqlserverwriter  yunque-1.0-SNAPSHOT-release.tar.gz
clickhousereader  db2writer  doriswriter          hivewriter   LICENSE         pgwriter      starrocksreader
clickhousewriter  dmreader   elasticsearchreader  jars         mysqlreader     pom.xml       starrockswriter
common            dmwriter   elasticsearchwriter  kafkareader  mysqlwriter     README.en.md  userGuid.md
conf              docker     hdfsreader           kafkawriter  oraclereader    README.md     version
```
创建运行目录并解压缩：
```
$ mkdir -p /usr/local/yunque-release
$ cd {yunque_source_code_home}
$ tar -xzvf yunque-1.0-SNAPSHOT-release.tar.gz -C /usr/local/yunque-release
```
运行目录结构如下：
```
bin  docker  docs  lib  LICENSE  README.en.md  README.md
```
### 3.配置示例

以mysql读写插件为例，创建配置文件 mysql2mysql.yaml 内容如下：
```
reader:
  plugin: "mysqlreader"
  url: "jdbc:mysql://localhost:3306/dev_from?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC"
  username: "root"
  password: "******"
  table: "book"
  column: "id,name"
  thread: "2"

writer:
  plugin: "mysqlwriter"
  url: "jdbc:mysql://localhost:3306/dev_to?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC"
  username: "root"
  password: "*******"
  table: "book"
  column: "id,name"
  thread: "1"

kafka:
  host: localhost:9092
  topic: hu_topic
  clientId: hu_client
  groupId: hu_group

log:
  logPath: /temp/data/hulogs

```

### 4.启动

```
$ sh start.sh -j test -i 12 -p mysql2mysql.yaml -f 'YAML'

```
> -j -- job 任务名称 -i -- jobId 任务ID -p -- path 任务文件路径 -f -- fileFormat 文件类型

### 5.查看日志

```
$ cat ${baseDir}/logs/${jobId}/run.log
```