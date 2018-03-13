package com.lvbby.codema.java;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.lvbby.codema.java.tool.MavenConfig;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 *
 * @author dushang.lp
 * @version $Id: JavaTest.java, v 0.1 2018年03月13日 下午1:24 dushang.lp Exp $
 */
public class JavaTest {
     @Test
     public void maven() throws Exception {
         MavenConfig parse = MavenConfig.parse("/Users/dushang.lp/workspace/project/codema");
         parse.scanChild();
         System.out.println(JSON.toJSONString(parse,true));
     }
     @Test
     public void mavenError() throws Exception {
         MavenConfig parse = MavenConfig.parse("/Users/dushang.lp/workspace/fincontract/static/css/base");
         parse.scanChild();
         System.out.println(JSON.toJSONString(parse,true));
     }
     @Test
     public void scan() throws Exception {
         List<MavenConfig> parse = MavenConfig.scan(Lists.newArrayList(new File("/Users/lipeng/workspace/codema")));
         System.out.println(JSON.toJSONString(parse,true));

         MavenConfig mavenConfig = parse.get(0);
         for (MavenConfig config : mavenConfig.toList()) {
             System.out.println(config.getArtifactId());
         }
     }
}