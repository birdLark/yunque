#!/bin/bash

# 脚本环境变量
export WORKPATH=$( cd $(dirname $0)/../ ;pwd)
export LIBPATH="${WORKPATH}/lib"

# java 环境变量
export JAVACMD=$(which java| awk '{print $1 }')

# 启动命令（默认为JSON）
${JAVACMD} -server -cp "${LIBPATH}/*"  com.larkmidtable.yunque.YunQueJSONStart
