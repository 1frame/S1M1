**1、Mybatis动态sql是做什么的？都有哪些动态sql？简述一下动态sql的执行原理？**

可在xml映射文件中编写动态sql，根据表达式的值完成逻辑判断并动态拼接sql。
共有9中sql标签：trim、where、set、foreach、if、choose、when、otherwise、bind
执行原理：使用OGNL表达式，从sql参数对象中计算表达式的值，根据表达式的值动态拼接sql，从而实现该功能。



**2、Mybatis是否支持延迟加载?如果支持,它的实现原理是什么?**

Mybatis 仅支持 association 关联对象和 collection 关联集合对象的延迟加载，association 指的就是一对一，collection 指的就是一对多查询。在 Mybatis配置文件中，可以配置是否启用延迟加载 lazyLoadingEnabled=true|false。它的原理是，使用 CGLIB 创建目标对象的代理对象，当调用目标方法时，进入拦截器方法，拦截器方法中查找到null值的变量时，会单独发送实现保存好的sql来查询到改变了，并赋值，于是便可以获取该变量的值了。



**3、Mybatis都有哪些Executor执行器？它们之间的区别是什么？**

有SimpleExecutor、ReuseExecutor、BatchExecutor、CachingExecutor 

SimpleExecutor：默认的，普通执行器。每执行一次update或select语句，开启一个Statement对象，用完立刻关闭Statement对象。

ReuseExecutor：重用语句并执行批量更新，执行update或select，以sql作为key查找Statement对象，存在就使用，不存在就创建，用完后，不关闭Statement对象，而是放置于Map内，供下一次使用。

BatchExecutor：重用预处理语句prepared statements。执行update（没有select，JDBC批处理不支持select），将所有sql都添加到批处理中（addBatch()），等待统一执行（executeBatch()），它缓存了多个Statement对象，每个Statement对象都是addBatch()完毕后，等待逐一执行executeBatch()批处理。与JDBC批处理相同。

CachingExecutor 是对以上三种Executor的包装，在 BaseExecutor 实现上包装了一层二级缓存，cacheEnabled=true时并且配置cache则开去全局的缓存。



**4、简述下Mybatis的一级、二级缓存（分别从存储结构、范围、失效场景。三个方面来作答）？**

1）一级缓存：Mybatis的一级缓存是指SqlSession级别的，作用域是SqlSession，Mybatis默认开启一级缓存，在同一个SqlSession中，相同的Sql查询的时候，第一次查询的时候，就会从缓存中取，如果发现没有数据，那么再去数据库查询，并且缓存到HashMap中，如果下次还是相同的查询，就直接从缓存中查询，不再去查询数据库，对应的就不再执行SQL语句。当进行增删改的操作的时候，缓存将会清空。

2）二级缓存：二级缓存是mapper级别的缓存，多个SqlSession去操作同一个mapper的sql语句，多个SqlSession可以共用二级缓存，二级缓存是跨SqlSession。第一次调用mapper下的sql的时候去查询信息，查询到的信息会存放到该mapper对应的二级缓存区域，第二次调用相同namespace下的mapper映射文件中，相同的SQL去查询，并去对应的二级缓存内取结果，如果在相同的namespace下的mapper映射文件中进行增删改操作，并且提交了事务，就二级缓存会被清空。



**5、简述Mybatis的插件运行原理，以及如何编写一个插件？**

Mybatis可以编写针对Executor、StatementHandler、ParameterHandler、ResultSetHandler四个接口的插件，mybatis使用JDK的动态代理为需要拦截的接口生成代理对象，然后实现接口的拦截方法，所以当执行需要拦截的接口方法时，会进入拦截方法（AOP面向切面编程的思想）。

如何编写插件：

1.编写Intercepror接口的实现类

```java
@Intercepts(@Signature(type = StatementHandler.class ,method="query" ,args= {Statement.class, ResultHandler.class}))
public class NewPlugin implements Interceptor {
  @Override
  public Object intercept(Invocation invocation) throws Throwable {	
    try {
      System.out.println("前置增强");
	   return invocation.proceed();  
    } finally {
      System.out.println("后置增强");
    }
  }
  @Override
  public Object plugin(Object target) {
    return  Plugin.wrap(target, this);
  } 
  public void setProperties(Properties properties) {  }

}
```

2.设置插件的签名，告诉mybatis拦截的具体对象的具体方法

3.将插件注册到全局配置文件中