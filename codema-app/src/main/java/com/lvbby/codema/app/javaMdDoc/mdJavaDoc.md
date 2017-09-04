## 接口
### 接口定义
`${@from.classFullName()}`
```java
${method}
```
### 参数
<% for( p in parameters){ %>
```java
${p}
```
<%}%>
<%if(result !=null){%>
### 结果
```java
${result}
```
<%}%>