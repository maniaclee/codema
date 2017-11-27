package com.lvbby.codema.java;

import com.lvbby.codema.app.mybatis.SqlBuilder;
import org.junit.Test;

/**
 * Created by lipeng on 2016/12/24.
 */
public class SqlBuilderTest extends BaseTest {

    @Test
    public void name() throws Exception {
        String table = "test";
        print(SqlBuilder.parseFromSpringDataStyle("selectByOrderIdAndIdOrderByGmtCreateTimeAndNameAsc").getSql(table));
        print(SqlBuilder.parseFromSpringDataStyle("queryByOrderIdAndIdOrderByGmtCreateTimeAndNameAsc").getSql(table));
    }

}
