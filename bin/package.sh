# java环境变量
if [ $(which java 2>/dev/null |wc -l) -eq 0 ]
then
   echo "请配置java环境。."
   exit 1
fi

if [ $(which mvn 2>/dev/null |wc -l) -eq 0 ]
then
   echo "请配置mvn环境。."
   exit 1
fi

export JAVACMD=$(which java|awk '{print $1}')
export MVNCMD=$(which  mvn |awk '{print $1}')

# workpath
WORKPATH=$(cd $(dirname $0);cd ../;pwd)

# import jar lib
echo ${MVNCMD} install:install-file -DgroupId=com.oracle -DartifactId=ojdbc6 -Dversion=11.2.0.3 -Dpackaging=jar -Dfile=${WORKPATH}/jars/ojdbc6-11.2.0.3.jar
echo ${MVNCMD} install:install-file -DgroupId=com.kingbase8 -DartifactId=kingbase8 -Dversion=8.6.0 -Dpackaging=jar -Dfile=${WORKPATH}/jars/kingbase8-8.6.0.jar

cd $WORKPATH
${MVNCMD} clean package -DskipTests

if [ ! -d /data/app/YunQue ]
then
    mkdir -p /data/app/YunQue
fi

tar -xvzf $WORKPATH/yunque-*-release.tar.gz -C /data/app/YunQue