## �ӿ�
### �ӿڶ���
`${@from.classFullName()}`
```java
${method}
```
### ����
<% for( p in parameters){ %>
```java
${p}
```
<%}%>
<%if(result !=null){%>
### ���
```java
${result}
```
<%}%>