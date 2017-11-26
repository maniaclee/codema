/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.lvbby.codema.app.mybatis;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import com.lvbby.codema.core.utils.CaseFormatUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 *
 * @author dushang.lp
 * @version $Id: SqlBuilder.java, v 0.1 2017-11-24 下午6:25 dushang.lp Exp $
 */
public class SqlBuilder {
    String method;
    List<String> wheres   = Lists.newLinkedList();
    List<String> orderBys = Lists.newLinkedList();
    String       sort     = "desc";

    public static SqlBuilder parseFromSpringDataStyle(String args) {
        SqlBuilder sql = new SqlBuilder();
        //1. 按OrderBy分割
        String[] sentences = args.split("OrderBy");
        String[] ss = sentences[0].split("By");
        sql.method = ss[0];
        //2. where语句
        sql.wheres = Arrays.stream(ss[1].split("And"))
                .map(s -> String.format("%s = #{%s}", underScore(s), camel(s)))
                .collect(Collectors.toList());
        if (sentences.length == 2) {
            //3. 排序
            Matcher matcher = Pattern.compile("(?<orderBys>.*)(?<sort>(Desc|Asc)$)?").matcher(sentences[1]);
            //4. order by
            if (matcher.find()) {
                sql.orderBys = Arrays.stream(matcher.group("orderBys").split("And")).map(s -> underScore(s)).collect(Collectors.toList());
                String sort = matcher.group("sort");
                if (StringUtils.isNotBlank(sort)) {
                    sql.sort = sort;
                }
            }
        }
        return sql;
    }

    private static String underScore(String s) {
        return CaseFormatUtils.toCaseFormat(CaseFormat.LOWER_UNDERSCORE, s);
    }

    private static String camel(String s) {
        return CaseFormatUtils.toCaseFormat(CaseFormat.LOWER_CAMEL, s);
    }

    public String getSql(String table) {
        String w = wheres.isEmpty() ? "" : String.format("where %s", wheres.stream().collect(Collectors.joining(" and ")));
        String ob = orderBys.isEmpty() ?
                "" :
                String.format("order by  %s %s", orderBys.stream().collect(Collectors.joining(",")), sort);
        return String.format("select * from %s %s %s", table, w, ob);
    }
}