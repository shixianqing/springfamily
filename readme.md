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