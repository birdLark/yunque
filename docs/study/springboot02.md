1.parent，需要继承spring-boot-starter-parent，减少依赖冲突

2.starter，依赖spring-boot-starter-web，以达到减少依赖配置的目的

3.引导类，自定义引导类，初始化Spring容器，扫描引导类所在包加载bean，当前包和子包

4.内嵌tomcat，依赖的spring-boot-starter-web中依赖的有tomcat

5.application.properties文件中，配置端口 
server.port=80

6.整合Mybatis
1.pom中导入start配置
2.书写相关的数据库信息
3.数据库的SQL映射添加@Mapper被容器识别到






