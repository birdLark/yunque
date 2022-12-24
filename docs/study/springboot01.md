# springboot的笔记1

1.选择File-->new--->Module

![](E:\larksource\A_open\ee\yunque\docs\study\image\f1.jpg)

2.选择maven项目

![](E:\larksource\A_open\ee\yunque\docs\study\image\f2.jpg)

3.填写对应的groupId,ArtifactId,version

![](E:\larksource\A_open\ee\yunque\docs\study\image\f3.jpg)

4.点击Finish创建项目完成

![](E:\larksource\A_open\ee\yunque\docs\study\image\f4.jpg)

5.pom文件中添加

```
<parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.5.4</version>
</parent>

<dependencies>
       
       <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
</dependencies>
```

6.创建start类 ---YunQueWebStart

```
@SpringBootApplication
public class YunQueWebStart {
	public static void main(String[] args) {
		SpringApplication.run(YunQueWebStart.class);
	}
}
```

7.创建controller类--WebController

```
@RestController
@RequestMapping("/yunque")
public class WebController {

	@GetMapping
	public String getById(){
		System.out.println("YunQue is running");
		return "YunQue getById";
	}
}
```

8.启动YunQueWebStart类，可以看到第一个springboot项目已经创建并启动了。

访问  http://localhost:8080/yunque