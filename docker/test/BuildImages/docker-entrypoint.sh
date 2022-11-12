#!/bin/bash
. /etc/profile
# 环境变量修改配置
#if [[ $redis_ip ]]; then
#	sed -i 's/redis_ip="[0-9.]*"/redis_ip="'$redis_ip'"/' config.ini
#fi
#
#if [[ $redis_port ]]; then
#	sed -i 's/redis_port="[0-9]*"/redis_port="'$redis_port'"/' config.ini
#fi

# 修改系统限制等等。。
#echo "1" > /proc/sys/kernel/core_uses_pid
#echo $CORE_PATH"/core-%e-%p-%t" > /proc/sys/kernel/core_pattern

# 执行启动命令
echo "执行启动命令 : $@"
exec "$@"

