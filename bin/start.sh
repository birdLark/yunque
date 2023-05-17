#!/bin/bash

# Copyright 1999-2018 Alibaba Group Holding Ltd.
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at

#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

[ ! -e "$JAVA_HOME/bin/java" ] && JAVA_HOME=$HOME/jdk/java
[ ! -e "$JAVA_HOME/bin/java" ] && JAVA_HOME=/usr/java
[ ! -e "$JAVA_HOME/bin/java" ] && JAVA_HOME=/opt/taobao/java
[ ! -e "$JAVA_HOME/bin/java" ] && unset JAVA_HOME

if [ -z "$JAVA_HOME" ]; then
  if $darwin; then

    if [ -x '/usr/libexec/java_home' ] ; then
      export JAVA_HOME=`/usr/libexec/java_home`

    elif [ -d "/System/Library/Frameworks/JavaVM.framework/Versions/CurrentJDK/Home" ]; then
      export JAVA_HOME="/System/Library/Frameworks/JavaVM.framework/Versions/CurrentJDK/Home"
    fi
  else
    JAVA_PATH=`dirname $(readlink -f $(which javac))`
    if [ "x$JAVA_PATH" != "x" ]; then
      export JAVA_HOME=`dirname $JAVA_PATH 2>/dev/null`
    fi
  fi
  if [ -z "$JAVA_HOME" ]; then
        error_exit "Please set the JAVA_HOME variable in your environment, We need java(x64)! jdk8 or later is better!"
  fi
fi

export SERVER="com.larkmidtable.yunque.YunQueEngine"

export JOB=""
export JOB_ID=""
export FILE_PATH=""
export FILE_FORMAT=""

while getopts ":j:i:f:p:" opt
do		
    case $opt in
        j)
            JOB=$OPTARG;;
        i)
            JOB_ID=$OPTARG;;
        p)
	          FILE_PATH=$OPTARG;;
        f)
            FILE_FORMAT=$OPTARG;;
        ?)
        echo "Unknown parameter"
        exit 1;;
    esac
done

echo ${JOB} ${JOB_ID} ${FILE_PATH} ${FILE_FORMAT}

export JAVA_HOME
export JAVA="$JAVA_HOME/bin/java"
export BASE_DIR=`cd $(dirname $0)/..; pwd`

#===========================================================================================
# JVM Configuration
#===========================================================================================
JAVA_OPT="${JAVA_OPT} -server -Xms512m -Xmx512m -Xmn512m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=320m"
JAVA_OPT="${JAVA_OPT} -XX:-OmitStackTraceInFastThrow -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${BASE_DIR}/logs/java_heapdump.hprof"
JAVA_OPT="${JAVA_OPT} -XX:-UseLargePages"

JAVA_MAJOR_VERSION=$($JAVA -version 2>&1 | sed -E -n 's/.* version "([0-9]*).*$/\1/p')
if [[ "$JAVA_MAJOR_VERSION" -ge "9" ]] ; then
  JAVA_OPT="${JAVA_OPT} -Xlog:gc*:file=${BASE_DIR}/logs/yunque_gc.log:time,tags:filecount=10,filesize=100m"
else
  JAVA_OPT="${JAVA_OPT} -XX:+UseConcMarkSweepGC -XX:+UseCMSCompactAtFullCollection -XX:CMSInitiatingOccupancyFraction=70 -XX:+CMSParallelRemarkEnabled -XX:SoftRefLRUPolicyMSPerMB=0 -XX:+CMSClassUnloadingEnabled -XX:SurvivorRatio=8  -XX:-UseParNewGC"
  JAVA_OPT_EXT_FIX="-Djava.ext.dirs=${JAVA_HOME}/jre/lib/ext:${JAVA_HOME}/lib/ext"
  JAVA_OPT="${JAVA_OPT} -Xloggc:${BASE_DIR}/logs/yunque_gc.log -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=100M"
fi

# lib
#JAVA_OPT="${JAVA_OPT} -cp '${BASE_DIR}/lib/*' '${SERVER}' -job ${JOB} -jobId ${JOB_ID} -path ${FILE_PATH} -fileFormat ${FILE_FORMAT}"

# 启动
if [ ! -d "${BASE_DIR}/logs/${JOB_ID}" ]; then
  mkdir -p ${BASE_DIR}/logs/${JOB_ID}
fi

echo "$JAVA $JAVA_OPT_EXT_FIX ${JAVA_OPT}"


# check the start.out log output file
if [ ! -f "${BASE_DIR}/logs/${JOB_ID}/run.log" ]; then
  touch "${BASE_DIR}/logs/${JOB_ID}/run.log"
fi
# pid 

if [ ! -d "${BASE_DIR}/pids/" ]; then
  mkdir -p ${BASE_DIR}/pids/
fi

PIDFILE=${BASE_DIR}/pids/${JOB_ID}.pid

if [ -f "$PIDFILE" ] && kill -0 $(cat "$PIDFILE"); then
  echo "job is already running..."
  exit 1
fi

#启动命令
#java "$JAVA_OPT_EXT_FIX" ${JAVA_OPT}
${JAVA} ${JAVA_OPT} -cp "${BASE_DIR}/lib/*" ${SERVER} -job ${JOB} -jobId ${JOB_ID} -path ${FILE_PATH} -fileFormat ${FILE_FORMAT} >> ${BASE_DIR}/logs/${JOB_ID}/run.log 2>&1 &
#${JAVA} ${JAVA_OPT} -cp "${BASE_DIR}/lib/*" ${SERVER} -job ${JOB} -jobId ${JOB_ID} -path ${FILE_PATH} -fileFormat ${FILE_FORMAT}

echo $! > $PIDFILE

echo "${JOB} start up success...."
