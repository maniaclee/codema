package com.lvbby.codema.core.machine.impl;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.fastjson.JSONObject;
import com.lvbby.codema.core.AbstractBaseMachine;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 把sql insert转为json
 * @author dushang.lp
 * @version $Id: JsonDataSourceMachine.java, v 0.1 2018年01月24日 下午6:14 dushang.lp Exp $
 */
public class SqlDataSourceMachine extends AbstractBaseMachine<String, JSONObject> {
    @Override
    protected void doCode() throws Exception {
        List<SQLStatement> sqlStatements = SQLUtils.parseStatements(source, JdbcConstants.MYSQL);
        List<JSONObject> results = sqlStatements.stream().map(sqlStatement -> {
            Validate.isTrue(sqlStatement instanceof SQLInsertStatement);
            SQLInsertStatement sql = (SQLInsertStatement) sqlStatement;
            JSONObject re = new JSONObject();
            List<Object> values = sql.getValues().getValues().stream().map(sqlExpr -> parseValue(sqlExpr.toString()))
                    .collect(Collectors.toList());
            List<String> columns = sql.getColumns().stream().map(SQLExpr::toString).collect(Collectors.toList());
            for (int i = 0; i < columns.size(); i++) {
                re.put(columns.get(i), values.get(i));
            }
            return re;
        }).collect(Collectors.toList());

        handleList(results , object -> new JsonResult(object));
    }

    private static Object parseValue(String s) {
        if ("null".equalsIgnoreCase(s)) {
            return null;
        }
        if (s.startsWith("'")) {
            s = s.substring(1, s.length() - 1);
            //date
            if (s.matches("\\d+-\\d+-\\d+.*")) {
                try {
                    return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(s);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            //String
            return s;
        }
        if (NumberUtils.isNumber(s)) {
            if (s.contains(".")) {
                return new BigDecimal(s);
            }
            return Integer.parseInt(s);
        }
        return s;
    }

}