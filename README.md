# Codema manual
## quick start
```java
public static void main(String[] args) throws Exception {
    MavenDirectoryScanner mavenDirectoryScanner = new MavenDirectoryScanner(Lists.newArrayList(new File(System.getProperty("user.home"), "workspace")));
    File dir = mavenDirectoryScanner.getMavenDirectories().stream().filter(file -> file.getAbsolutePath().endsWith("codema")).findAny().orElse(null);
    System.out.println(dir.getAbsolutePath());
    CodemaDocMachine codemaDocMachine = new CodemaDocMachine();
    codemaDocMachine.setDestRootDir(dir.getAbsolutePath());
    codemaDocMachine.addResultHandler(ResultHandlerFactory.fileWrite).addResultHandler(ResultHandlerFactory.print).run();
}
```
## source工厂类
* com.lvbby.codema.java.machine.JavaSourceMachineFactory
* com.lvbby.codema.core.tool.mysql.SqlMachineFactory
## ResultHandler工厂
* com.lvbby.codema.core.handler.ResultHandlerFactory

## machine
* com.lvbby.codema.app.bean.JavaBeanMachine
* com.lvbby.codema.app.charset.CharsetMachine
* com.lvbby.codema.app.codemadoc.CodemaDocMachine
* com.lvbby.codema.app.convert.JavaConvertMachine
* com.lvbby.codema.app.delegate.JavaDelegateMachine
* com.lvbby.codema.app.interfaces.JavaInterfaceMachine
* com.lvbby.codema.app.javaMdDoc.JavaMdDocEasyMachine
* com.lvbby.codema.app.javaMdDoc.JavaMdDocMachine
* com.lvbby.codema.app.mvn.MavenMachine
* com.lvbby.codema.app.mybatis.MybatisMachine
* com.lvbby.codema.app.mysql.BaseSqlMachine
* com.lvbby.codema.app.mysql.MysqlInsertMachine
* com.lvbby.codema.app.mysql.MysqlSchemaMachine
* com.lvbby.codema.app.mysql.SqlInsertToRecordMachine
* com.lvbby.codema.app.mysql.SqlSelectColumnsMachine
* com.lvbby.codema.app.mysql.SqlUpdateMachine
* com.lvbby.codema.app.repository.JavaRepositoryMachine
* com.lvbby.codema.app.simple.JsonMachine
* com.lvbby.codema.app.snippet.BasicJavaCodeMachine
* com.lvbby.codema.app.snippet.JavaBuilderMachine
* com.lvbby.codema.app.snippet.JavaRequestSettingMachine
* com.lvbby.codema.app.springboot.JavaSpringBootMachine
* com.lvbby.codema.app.testcase.JavaTestcaseMachine
* com.lvbby.codema.app.testcase.mock.JavaMockTestMachine
