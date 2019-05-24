## 登录h2数据库控制台
http://localhost:8080/h2-console

## mybatis相关问题
代码参考
[https://github.com/shixianqing/springparent/tree/master/datasourcedemo/src/main/java/com/example/mapperExt](https://github.com/shixianqing/springparent/tree/master/datasourcedemo/src/main/java/com/example/mapperExt)
### mybatis mapper.xml文件继承

1. 主mapper接口不要加注解，在子类上加上@Mapper注解
2. 父mapper.xml与子mapper.xml中的命名空间保持一致，即子mapper接口的全路径
3. 子mapper.xml能复用父mapper.xml里的内容
![](https://i.imgur.com/MsnSnhi.png)
![](https://i.imgur.com/N35Ntc3.png)
![](https://i.imgur.com/dbhNZBD.png)
![](https://i.imgur.com/UL6ahNm.png)

### mybatis获取新增数据的自增id值
1. 第一种方式
    
	    
	    <insert id="insert" parameterType="Coffee" useGeneratedKeys="true" keyColumn="id" keyProperty="id">
	   
	

	- useGeneratedKeys="true" ----使用自增主键
	- keyColumn="id" --- 主键在数据库中的列名
	- keyProperty="id" ------- 主键在javabean中的属性名


	
2. 第二种方式

		  <insert id="insert" parameterType="Coffee">
		  
		      <selectKey keyColumn="id" keyProperty="id">
		          SELECT LAST_INSERT_ID()
		      </selectKey>
       
    	  </insert>
    	  
### 生成扩展类插件待解决？？

### springboot继承mybatis，实体类别名设置、加载sqlMap文件

        mybatis.mapper-locations=classpath:mapper/*.xml //加载sqlxml文件
        mybatis.type-aliases-package=com.example.mapperExt.model //设置实体类所在包，实体类的别名即是实体类的名字
        
  mybatis.type-aliases-package=com.example.mapperExt.model    Coffee类在mapper.xml中的别名就叫Coffee

## druid
1. springboot集成druid
    2. 配置文件参数配置
           spring.datasource.druid.url=jdbc:h2:~/primary-database;AUTO_SERVER=TRUE;DB_CLOSE_ON_EXIT=FALSE
             spring.datasource.druid.username=test1
             spring.datasource.druid.password=test1
             spring.datasource.druid.driver-class-name=org.h2.Driver
             mybatis.mapper-locations=classpath:mapper/*.xml
              mybatis.type-aliases-package=com.example.model
              spring.datasource.type=com.alibaba.druid.pool.DruidDataSource
                #最大活跃连接数
                spring.datasource.druid.maxActive=20 
                #初始化连接数
                spring.datasource.druid.initialSize=1
                #获取连接最大等待时间
                spring.datasource.druid.maxWait=60000
                #最小活跃连接数
                spring.datasource.druid.minIdle=1
                #配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
                spring.datasource.druid.timeBetweenEvictionRunsMillis=2000 
                #最小存活时间
                spring.datasource.druid.minEvictableIdleTimeMillis=600000 
    2. 代码配置
          
            @Configuration
            public class DruidDataSourceConfig {
                /**
                 * ConfigurationProperties 注解可以将配置文件中定义的属性自动封装到实体中
                 * @return
                 */
                @Bean
                @Primary
                @ConfigurationProperties(prefix = "spring.datasource.druid")
                public DataSource druidDataSource(){
                    DruidDataSource druidDataSource = new DruidDataSource();
                    return druidDataSource;
                }
            
            }
2. druid 监控
     1. 日志输出监控
        . 配置文件参数
              在druid-xxx.jar!/META-INF/druid-filter.properties文件中描述了这四种Filter的别名

            spring.datasource.druid.filters=stat,slf4j,wall
            spring.datasource.druid.filter.slf4j.enabled=true
. 配置logback.xml


            <?xml version="1.0" encoding="UTF-8"?>
            <configuration debug="true">
                        <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
                            <filter class="ch.qos.logback.classic.filter.ThresholdFilter">   
                              <level>DEBUG</level>
                            </filter>       
                            <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder"> 
                                <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{50} [%file : %line] - %msg%n</pattern>   
                            </encoder> 
                        </appender>
                        <appender name="FILE"  class="ch.qos.logback.core.rolling.RollingFileAppender"> 
                            <filter class="ch.qos.logback.classic.filter.ThresholdFilter">   
                              <level>INFO</level>
                            </filter>    
                            
                        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
                           <FileNamePattern>/opt/dev/log/claimtrial/claimtrial.%d{yyyy-MM-dd}.%i.log</FileNamePattern>
                           <maxFileSize>100MB</maxFileSize>    
                           <maxHistory>30</maxHistory>
                           <totalSizeCap>10GB</totalSizeCap>
                        </rollingPolicy>
                        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder"> 
                            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} [%file : %line] - %msg%n</pattern> 
                            <charset>UTF-8</charset>              
                        </encoder> 
                    </appender> 
                    <!--myibatis log configure--> 
                    <logger name="com.example" level="DEBUG"/>
                    <logger name="org.springframework" level="WARN"/>
                    <logger name="org.apache.ibatis.logging.jdbc.BaseJdbcLogger" level="DEBUG"/>
                    <logger name="org.mybatis" level="DEBUG"/>
                    <logger name="java.sql.Connection" level="DEBUG"/>
                    <logger name="java.sql.Statement" level="DEBUG"/>
                    <logger name="java.sql.PreparedStatement" level="DEBUG"/>
                    <logger name="java.sql.ResultSet" level="DEBUG"/>
                    <root level="DEBUG">
                        <appender-ref ref="FILE" />
                        <appender-ref ref="STDOUT" />
                    </root> 
            </configuration>

       
    2. druid控制台页面监控     
        Druid内置提供了一个StatViewServlet用于展示Druid的统计信息。 这个StatViewServlet的用途包括： 提供监控信息展示的html页面、 提供监控信息的JSON API
    Druid内置提供一个WebStatFilter用于sql监控统计
        1. 配置文件参数配置
                spring.datasource.druid.filters=stat

        2.    servlet配置
                @WebServlet(urlPatterns = {"/druid/*"},initParams = {
                @WebInitParam(name = "loginPassword",value = "admin"),
                @WebInitParam(name = "loginPassword",value = "admin"),
                @WebInitParam(name = "resetEnable",value = "false")
                })
                public class DruidServlet extends StatViewServlet {
                
                }

        3. filter配置
                @WebFilter(urlPatterns = "/*",initParams = {
                @WebInitParam(name = "exclusions",value = "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico")
                })
                public class DruidFilter extends WebStatFilter {
                }
 
        4.  访问监控页面
            http://localhost:8080/druid/index.html
     