##登录h2数据库控制台
http://localhost:8080/h2-console

## mybatis mapper.xml文件继承

1. 主mapper接口不要加注解，在子类上加上@Mapper注解
2. 父mapper.xml与子mapper.xml中的命名空间保持一致，即子mapper接口的全路径
3. 子mapper.xml能复用父mapper.xml里的内容
4. 例子：![](https://i.imgur.com/MsnSnhi.png)