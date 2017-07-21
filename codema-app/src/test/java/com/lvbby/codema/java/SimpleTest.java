package com.lvbby.codema.java;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSQLColumnDefinition;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.util.JdbcConstants;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.PrimitiveType;
import com.google.common.collect.Lists;
import com.lvbby.codema.app.bean.JavaBeanCodemaConfig;
import com.lvbby.codema.app.mybatis.$src__name_Dao;
import com.lvbby.codema.core.Codema;
import com.lvbby.codema.core.engine.ScriptEngineFactory;
import com.lvbby.codema.core.render.TemplateEngineFactory;
import com.lvbby.codema.core.utils.ReflectionUtils;
import com.lvbby.codema.java.engine.JavaEngineContext;
import com.lvbby.codema.java.tool.JavaLexer;
import com.lvbby.codema.java.tool.JavaSrcLoader;
import org.apache.commons.io.IOUtils;
import org.joor.Reflect;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.List;

/**
 * Created by lipeng on 2016/12/24.
 */
public class SimpleTest {

    public static int a = 1;

    @Test
    public void uri() {
        URI uri = URI.create("asdf:///root/leaf?path=sdf&&sub=sdf");
        System.out.println(uri.getAuthority());
        System.out.println(uri.getPath());
        System.out.println(uri.getQuery());
        System.out.println(uri);
    }

    @Test
    public void name() throws Exception {
        JavaEngineContext parameter = new JavaEngineContext();
        parameter.setFromClassName("shitDto");
        //        String eval = ScriptEngineFactory.instance.eval("script://js/{match: /.*DTO/i.test($fromClassName), result: $fromClassName.replace(/DTO/i, 'Entity')}}", parameter);
        String eval = ScriptEngineFactory.instance.eval("script://js/  (function(){return {match: /.*DTO/i.test($fromClassName), result: $fromClassName.replace(/DTO/i, 'Entity')}})()", parameter);
        System.out.println(eval);

    }

    @Test
    public void yaml() throws Exception {
        Yaml yaml = new Yaml();
        A a = new A();
        a.setName("sdf");
        A aa = new A();
        aa.setName("aa");
        a.setA(Lists.newArrayList(aa));
        System.out.println(yaml.dump(a));
    }

    @Test
    public void beanProperty() throws Exception {
        ReflectionUtils.getAllFields(Codema.class, null).forEach(field -> System.out.println(field.getName()));
    }

    @Test
    public void methods() throws Exception {
        for (Method method : ReflectionUtils.getAllMethods(Codema.class)) {
            System.out.println(method.getName());
        }
        String s = "public Codema addCodemaMachine(SourceParser sourceParser) {\n" +
                "        this.sourceParserFactory.getSourceParsers().add(sourceParser);\n" +
                "        return this;\n" +
                "    }";
        String field = "sourceParserFactory";
        List<Object> allConvert = ReflectionUtils.findAllConvert(s, field + "\\.([^\\(\\)]+)\\(", matcher -> matcher.group(1));
        System.out.println(allConvert);
    }


    class A {
        private List<A> a;
        private String name;


        public List<A> getA() {
            return a;
        }

        public void setA(List<A> a) {
            this.a = a;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


    @Test
    public void java() throws Exception {
        System.out.println(getClass().getResource("/"));
        System.out.println(getClass().getResource("/").getFile().toString());
        System.out.println(getJavaSource(JavaLexer.class));
    }

    public String getJavaSource(Class clz) throws IOException {
        String path = "/" + clz.getName().replace('.', '/') + ".java";
        System.out.println(path);
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(path);
        return IOUtils.toString(resourceAsStream);
    }

    @Test
    public void sql() throws Exception {

        // String sql = "update t set name = 'x' where id < 100 limit 10";
        // String sql = "SELECT ID, NAME, AGE FROM USER WHERE ID = ? limit 2";
        // String sql = "select * from tablename limit 10";

        String sql = "CREATE TABLE `SB_Article_Audit_Interview` (\n" +
                "  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '数据表id',\n" +
                "  `article_id` bigint(11) NOT NULL COMMENT '文章id',\n" +
                "  `guest_name` varchar(50) DEFAULT NULL COMMENT '嘉宾姓名',\n" +
                "  `guest_avatar` varchar(200) DEFAULT NULL COMMENT '嘉宾头像',\n" +
                "  `guest_desc` varchar(1000) DEFAULT NULL COMMENT '嘉宾描述',\n" +
                "  `ask_end_time` datetime DEFAULT NULL COMMENT '提问截止时间（日期格式）',\n" +
                "  `background_type` smallint(4) NOT NULL COMMENT '背景图片类型',\n" +
                "  `join_num` int(11) NOT NULL COMMENT '参加人数',\n" +
                "  `result_url` varchar(500) DEFAULT '' COMMENT '访谈全文url',\n" +
                "  `result_pices` varchar(5000) NOT NULL DEFAULT '' COMMENT '访谈过程中图片(地址数组)',\n" +
                "  `result_context` varchar(5000) NOT NULL DEFAULT '' COMMENT '访谈摘要',\n" +
                "  `result_head_pic` varchar(500) DEFAULT '' COMMENT '访谈结果新闻头图',\n" +
                "  `result_video_url` varchar(500) DEFAULT NULL COMMENT '访谈视频url',\n" +
                "  `status` smallint(6) NOT NULL DEFAULT '1' COMMENT '访谈状态',\n" +
                "  `ad_pic` varchar(5000) DEFAULT '' COMMENT '广告页图片',\n" +
                "  `brand_desc` text COMMENT '品牌说明',\n" +
                "  `create_time` datetime NOT NULL COMMENT '创建时间',\n" +
                "  `updatetime` datetime NOT NULL COMMENT '更新时间',\n" +
                "  `background` text COMMENT '背景',\n" +
                "  `source_pic` text COMMENT '来源logo图片',\n" +
                "  PRIMARY KEY (`id`),\n" +
                "  UNIQUE KEY `UX_articleId` (`article_id`),\n" +
                "  KEY `updatetime` (`updatetime`),\n" +
                "  KEY `ix_create_time` (`create_time`)\n" +
                ") ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='访谈型文章扩展表';";
        String dbType = JdbcConstants.MYSQL;

        //格式化输出
        String result = SQLUtils.format(sql, dbType);
        System.out.println(result); // 缺省大写格式
        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);
        MySqlCreateTableStatement create = (MySqlCreateTableStatement) stmtList.get(0);
        String table = create.getTableSource().toString();
        List<SQLTableElement> tableElementList = create.getTableElementList();
        for (SQLTableElement sqlTableElement : tableElementList) {
            MySqlSQLColumnDefinition column = (MySqlSQLColumnDefinition) sqlTableElement;
            SQLName name = column.getName();
            SQLExpr comment = column.getComment();
            for (SQLColumnConstraint sqlColumnConstraint : column.getConstraints()) {
                if (sqlColumnConstraint instanceof NotNullConstraint) {

                }
                if (sqlColumnConstraint instanceof SQLColumnPrimaryKey) {

                }
                if (sqlColumnConstraint instanceof SQLColumnUniqueKey) {

                }
            }
            SQLDataType dataType = column.getDataType();
            String type = dataType.getName();//like bigint
        }
        //解析出的独立语句的个数
        System.out.println("size is:" + stmtList.size());
        for (int i = 0; i < stmtList.size(); i++) {

            SQLStatement stmt = stmtList.get(i);
            MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
            stmt.accept(visitor);

            //获取表名称
            System.out.println("Tables : " + visitor.getCurrentTable());
            //获取操作方法名称,依赖于表名称
            System.out.println("Manipulation : " + visitor.getTables());
            //获取字段名称
            System.out.println("fields : " + visitor.getColumns());
        }


    }

    @Test
    public void reflection() throws Exception {
        JavaBeanCodemaConfig javaBeanCodemaConfig = new JavaBeanCodemaConfig();
        javaBeanCodemaConfig.setAuthor("sdf");
        Object destSrcRoot = Reflect.on(javaBeanCodemaConfig).field("author").get();
        System.out.println(destSrcRoot);

    }

    @Test
    public void template() throws Exception {
        System.out.println(TemplateEngineFactory.create("\\${abc}").render());
    }

    @Test
    public void templateSrc() throws Exception {
        String format = String.format("templates/%s.java", $src__name_Dao.class.getName().replace('.', '/'));
        System.out.println(format);
        System.out.println(IOUtils.toString(JavaSrcLoader.getJavaSrc($src__name_Dao.class)));
    }

    @Test
    public void env() throws Exception {
        System.out.println(System.getProperty("user.home"));
        System.out.println(System.getProperty("user.name"));

    }

    @Test
    public void primitiveClass() throws Exception {
        System.out.println(boolean.class.getPackage());
        System.out.println(String.class.getClass());

        Field a = this.getClass().getDeclaredField("a");
        a.setAccessible(true);
        SimpleTest simpleTest = new SimpleTest();
        System.out.println(simpleTest.a);
        a.set(simpleTest, 3);
        System.out.println(simpleTest.a);

    }

    @Test
    public void commentAtEnd() throws Exception {
        VariableDeclarator variableDeclarator = new VariableDeclarator(PrimitiveType.booleanType(), "a");
        System.out.println(variableDeclarator);
    }
}
