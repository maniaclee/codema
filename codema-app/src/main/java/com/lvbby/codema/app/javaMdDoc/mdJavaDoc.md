<%if(javaMethod !=null){%>
#### 接口
##### 接口定义
`${@from.classFullName()}#${javaMethod.name}`
```java
${method}
```
#####  参数
<% for( p in parameters){ %>
 `${@p.classFullName()}`
```java
${p.src}
```
<%}%>
<%if(result !=null){%>
##### 结果
 `${@result.classFullName()}`
```java
${result.src}
```
<%}%>
<%}%>

<%if(javaMethod ==null){%>
```java
${type}
```
<%}%>
