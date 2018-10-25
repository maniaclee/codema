<%if(javaMethod !=null){%>
### 接口
#### 接口定义
`${source.classFullName}#${javaMethod.name}`
```java
${method}
```
####  参数
<% for( p in parameters){ %>
 `${p.classFullName}`
```java
${p.src}
```
<%}%>
<%if(result !=null){%>
#### 结果
 `${result.classFullName}`
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
