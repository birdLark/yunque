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
> 注意：如果是在windows系统编译打包的，bin下的*.sh脚本编码格式是dos，在Linux系统上运行时，需要使用 vim 文件名,然后set ff=unix 或 :set fileformat=unix改编码格式。

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

### 4.脚本授权并启动

```
$ chmod +x start.sh
$ sh start.sh -j test -i 1 -p ../mysql2mysql.yaml -f YAML

```
启动日志如下：
```
test 1 ../mysql2mysql.yaml YAML
/data/jdk1.8.0_181/bin/java -Djava.ext.dirs=/data/jdk1.8.0_181/jre/lib/ext:/data/jdk1.8.0_181/lib/ext  -server -Xms512m -Xmx512m -Xmn512m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=320m -XX:-OmitStackTraceInFastThrow -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/usr/local/yunque-release/logs/java_heapdump.hprof -XX:-UseLargePages -XX:+UseConcMarkSweepGC -XX:+UseCMSCompactAtFullCollection -XX:CMSInitiatingOccupancyFraction=70 -XX:+CMSParallelRemarkEnabled -XX:SoftRefLRUPolicyMSPerMB=0 -XX:+CMSClassUnloadingEnabled -XX:SurvivorRatio=8  -XX:-UseParNewGC -Xloggc:/usr/local/yunque-release/logs/yunque_gc.log -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=100M
test start up success....
```
> -j -- job 任务名称 -i -- jobId 任务ID -p -- path 任务文件路径 -f -- fileFormat 文件类型

### 5.查看日志

```
$ cat ${baseDir}/logs/${jobId}/run.log
```