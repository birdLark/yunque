
# docker

# 下载镜像
```shell
docker pull larkmidtable/honghu:1.0
```

# 运行测试
```shell
docker run -it --rm --name honghu-test larkmidtable/honghu:1.0
```

# 持久化目录/配置库变量/服务端口
```shell
docker run -it  \
-p 12345:12345 \
-v /data/honghu/config:/home/honghu/config \
-v /data/honghu/log:/home/honghu/log \
-v /data/honghu/data:/home/honghu/data \
--name honghu-test  \
-d larkmidtable/honghu:1.0
```
