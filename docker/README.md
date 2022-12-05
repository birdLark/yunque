
# docker

# 下载镜像
```shell
docker pull larkmidtable/yunque:1.0
```

# 运行测试
```shell
docker run -it --rm --name yunque-test larkmidtable/yunque:1.0
```

# 持久化目录/配置库变量/服务端口
```shell
docker run -it  \
-p 12345:12345 \
-v /data/yunque/config:/home/yunque/config \
-v /data/yunque/log:/home/yunque/log \
-v /data/yunque/data:/home/yunque/data \
--name yunque-test  \
-d larkmidtable/yunque:1.0
```
