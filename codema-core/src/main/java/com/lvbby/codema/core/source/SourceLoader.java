package com.lvbby.codema.core.source;

import java.util.List;

/**����source���������List���������Ƕ��source
 * Created by lipeng on 2017/8/19.
 */
public interface SourceLoader<T> {

    List<T> loadSource() throws Exception;

    /***
     * ��ȡ����������source����
     * @return
     */
    Class<T> getType();
}
