package com.lvbby.codema.app.mysql;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLNullExpr;
import com.alibaba.druid.sql.ast.expr.SQLValuableExpr;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.util.JdbcConstants;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lvbby.codema.core.AbstractBaseMachine;
import com.lvbby.codema.core.handler.ResultHandlerFactory;
import com.lvbby.codema.core.result.BasicResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * insert sql --> 行记录集合
 * @author dushang.lp
 * @version $Id: SqlInsertMachine.java, v 0.1 2017年12月04日 上午11:36 dushang.lp Exp $
 */
public class SqlInsertMachine extends AbstractBaseMachine<String,List<Map<String,Object>>>{
    @Override protected void doCode() throws Exception {
        List<SQLStatement> sqlStatements = SQLUtils.parseStatements(source, JdbcConstants.MYSQL);
        List<Map<String, Object>> result = sqlStatements.stream().map(sqlStatement -> {
            SQLInsertStatement insertStatement = (SQLInsertStatement) sqlStatement;
            List<String> columns = insertStatement.getColumns().stream()
                    .map(sqlExpr -> sqlExpr.toString()).collect(Collectors.toList());
            List<Object> values = insertStatement.getValues().getValues().stream()
                    .map(sqlExpr -> getValue(((SQLValuableExpr) sqlExpr)))
                    .collect(Collectors.toList());
            Map<String, Object> map = Maps.newHashMap();
            for (int i = 0; i < columns.size(); i++) {
                map.put(columns.get(i), values.get(i));
            }
            return map;
        }).collect(Collectors.toList());
        handle(BasicResult.instance(result));
    }
    private Object getValue(SQLValuableExpr expr)   {
        Object value = expr.getValue();
        if(expr instanceof SQLNullExpr){
            return null;
        }
        if(value instanceof String){
            if(value.toString().matches("\\d{4}-\\d{2}-\\d{2}.*")){
                try {
                    return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(value.toString());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return value;
    }


    public static void main(String[] args) throws Exception {
        String sql = "insert into `rt_evaluation_question`( question_id,  question_code,  title,  is_active,  version,  val_type,  question_type,  gmt_create,  gmt_modified) values('2017112200000001', '1', '刚到唐朝，身无分文，你会如何白手起家？', 'T', null, '2017112200000001', '0', '2017-11-22 20:16:29', '2017-11-22 20:16:50');\n"
                     + "insert into `rt_evaluation_question`( question_id,  question_code,  title,  is_active,  version,  val_type,  question_type,  gmt_create,  gmt_modified) values('2017112200000002', '2', '开元盛世，商业发达，你会经营什么？', 'T', null, '2017112200000001', '0', '2017-11-22 20:17:01', '2017-11-22 20:17:38');\n"
                     + "insert into `rt_evaluation_question`( question_id,  question_code,  title,  is_active,  version,  val_type,  question_type,  gmt_create,  gmt_modified) values('2017112200000003', '3', '3 如果要投资房地产，你选择哪个唐朝城市？', 'T', null, '2017112200000001', '0', '2017-11-22 20:19:04', '2017-11-22 20:19:39');\n"
                     + "insert into `rt_evaluation_question`( question_id,  question_code,  title,  is_active,  version,  val_type,  question_type,  gmt_create,  gmt_modified) values('2017112200000004', '3', '安史之乱很快到来，你要如何保护家财？', 'T', null, '2017112200000001', '0', '2017-11-22 20:19:56', '2017-11-22 20:22:16');\n";
        new SqlInsertMachine().source(sql).resultHandlers(Lists.newArrayList(ResultHandlerFactory.print)).run();
    }
}