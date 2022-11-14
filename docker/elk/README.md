## 一、调整内存：max virtual memory areas vm.max_map_count [65530] is too low, increase to at least [262144]（elasticsearch用户拥有的内存权限太小，至少需要262144）

#### 1.修改配置sysctl.conf
[root@localhost ~]# vi /etc/sysctl.conf
#### 2.添加下面配置：
vm.max_map_count=262144
#### 3.重新加载：
[root@localhost ~]# sysctl -p
#### 4.最后重新启动elasticsearch，即可启动成功。


## 二、Docker 命令自动补全
#### 1.安装依赖工具bash-complete
[root@localhost ~]# yum install -y bash-completion

[root@localhost ~]# source /usr/share/bash-completion/completions/docker

[root@localhost ~]# source /usr/share/bash-completion/bash_completion

## 三、将本文件夹内的文件拷贝至服务器
#### 1.对sh脚本赋予执行权限

#### 2.执行 ./deploy.sh

#### 3.等待服务启动完毕即可

#### 4.卸载执行 ./undeploy.sh