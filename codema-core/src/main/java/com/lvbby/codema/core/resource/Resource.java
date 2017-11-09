package com.lvbby.codema.core.resource;

import java.io.InputStream;

/**
 * 资源
 * @author dushang.lp
 * @version $Id: Resource.java, v 0.1 2017-11-09 下午11:03 dushang.lp Exp $$
 */
public interface Resource {
    InputStream getInputStream() throws Exception;
}