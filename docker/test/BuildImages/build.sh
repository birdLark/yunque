PROJECTHOME=/data/yunque
WORKHOME=docker/test/BuildImages
TARGETHOME=target
LIBHOME=${TARGETHOME}/lib
JDKPKG=${WORKHOME}/jdk1.8.0_202


cnt=$(ls -rt ${PROJECTHOME}/${TARGETHOME}/yunque*.jar  2>/dev/null | tail -1 | wc -l )
if [ $cnt = 1  ]
then
  JAR_NAME=$(ls -rt ${PROJECTHOME}/${TARGETHOME}/yunque-${version}*.jar | tail | xargs basename)
  JAR_FULL_NAME=${TARGETHOME}/${JAR_NAME}
else
  echo "yunque程序jar包不存在，请先编译打包: ${TARGETHOME}"
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
cat > ${PROJECTHOME}/${WORKHOME}/yunque-DockerFile <<DockerFile
FROM centos:7.9.2009
RUN (groupadd -g 3000 yunque)
RUN (useradd  -g 3000 -u 3000 yunque)
COPY --chown=3000:3000 ${WORKHOME}/yunque_profile.sh /etc/profile.d/

USER yunque
WORKDIR /home/yunque
RUN ( mkdir bin )
COPY --chown=3000:3000 ${WORKHOME}/docker-entrypoint.sh /home/yunque/bin/docker-entrypoint.sh
RUN ( chmod +x  /home/yunque/bin/docker-entrypoint.sh )
RUN ( mkdir demo )
RUN ( mkdir data )
RUN ( mkdir config )


COPY --chown=3000:3000 ${JDKPKG} /home/yunque/jdk

COPY --chown=3000:3000 ${LIBHOME} /home/yunque/lib/
COPY --chown=3000:3000 ${JAR_FULL_NAME} /home/yunque/


ENTRYPOINT ["/home/yunque/bin/docker-entrypoint.sh"]

CMD ["java","-jar", "/home/yunque/${JAR_NAME}"]

DockerFile
echo "end  -- DockerFile "


echo "begin -- build"
docker build -t larkmidtable/yunque:${version} -f ${PROJECTHOME}/${WORKHOME}/yunque-DockerFile ${PROJECTHOME}
docker push larkmidtable/yunque:${version}
echo "end -- build"
