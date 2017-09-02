# Java Template

## Quick start
```java
public void quickStart() throws Exception {
        //配置
        JavaBeanCodemaConfig config = new JavaBeanCodemaConfig();
        config.setAuthor("lee");
        config.setResultHandlers(Lists.newArrayList(new JavaRegisterResultHandler(), new PrintResultHandler()));
        config.setDestPackage("com.lvbby");
        config.setDestClassName("DestBean");

        //以JavaMockTestCodemaConfig.class为模板
        JavaClassSourceParser sourceLoader = JavaClassSourceParser.fromClass(JavaMockTestCodemaConfig.class);

        //执行
        Codema.exec(config, sourceLoader);

    }
```

## java template
默认的参数：

| 参数                        | 类型        | 说明   |
| ------------------------- | --------- | ---- |
| destClassName             | String    |      |
| src                       | JavaClass |      |
| from                      | Object    |      |
| srcClassName              | src.name  |      |
| srcClassNameUncapitalized | src.name  | 小写开头 |
| destClassName             | String    |      |
| config                    | config    |      |