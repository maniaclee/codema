package com.lvbby.codema.core.source;

import java.util.List;

/**解析source，如果返回List，则任务是多个source
 * Created by lipeng on 2017/8/19.
 */
public interface SourceLoader<T> {

    List<T> loadSource() throws Exception;
}
