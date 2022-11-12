PROJECTHOME=/data/honghu
WORKHOME=docker/test/BuildImages
TARGETHOME=target
LIBHOME=${TARGETHOME}/lib
JDKPKG=${WORKHOME}/jdk1.8.0_202


cnt=$(ls -rt ${PROJECTHOME}/${TARGETHOME}/honghu*.jar  2>/dev/null | tail -1 | wc -l )
if [ $cnt = 1  ]
then
  JAR_NAME=$(ls -rt ${PROJECTHOME}/${TARGETHOME}/honghu-${version}*.jar | tail | xargs basename)
  JAR_FULL_NAME=${TARGETHOME}/${JAR_NAME}
else
  echo "honghu程序jar包不存在，请先编译打包: ${TARGETHOME}"
  exit
fi


if [ x"$1" = x ]
then
  version=$(echo ${JAR_NAME} |awk -F"-" '{print $2}')
  echo " ${JAR_NAME} 默认版本号 : ${version}"
else
  version=$1
  echo " ${JAR_NAME} 指定版本号 : ${version}"
fi



echo "begin -- DockerFile"
cat > ${PROJECTHOME}/${WORKHOME}/honghu-DockerFile <<DockerFile
FROM centos:7.9.2009
RUN (groupadd -g 3000 honghu)
RUN (useradd  -g 3000 -u 3000 honghu)
COPY --chown=3000:3000 ${WORKHOME}/honghu_profile.sh /etc/profile.d/

USER honghu
WORKDIR /home/honghu
RUN ( mkdir bin )
COPY --chown=3000:3000 ${WORKHOME}/docker-entrypoint.sh /home/honghu/bin/docker-entrypoint.sh
RUN ( chmod +x  /home/honghu/bin/docker-entrypoint.sh )
RUN ( mkdir demo )
RUN ( mkdir data )
RUN ( mkdir config )


COPY --chown=3000:3000 ${JDKPKG} /home/honghu/jdk

COPY --chown=3000:3000 ${LIBHOME} /home/honghu/lib/
COPY --chown=3000:3000 ${JAR_FULL_NAME} /home/honghu/


ENTRYPOINT ["/home/honghu/bin/docker-entrypoint.sh"]

CMD ["java","-jar", "/home/honghu/${JAR_NAME}"]

DockerFile
echo "end  -- DockerFile "


echo "begin -- build"
docker build -t larkmidtable/honghu:${version} -f ${PROJECTHOME}/${WORKHOME}/honghu-DockerFile ${PROJECTHOME}
docker push larkmidtable/honghu:${version}
echo "end -- build"
