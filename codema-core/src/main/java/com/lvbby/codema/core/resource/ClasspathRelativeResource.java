package com.lvbby.codema.core.resource;

import java.io.InputStream;

/**
 *
 * @author dushang.lp
 * @version $Id: ClasspathRelativeResource.java, v 0.1 2018年03月07日 下午4:11 dushang.lp Exp $
 */
public class ClasspathRelativeResource extends  AbstractNamedResource{
    private String resource;
    private Class  clz;

    public ClasspathRelativeResource(String resource, Class clz) {
        this.resource = resource;
        this.clz = clz;
        setResourceName(resource);
    }

    @Override
    public InputStream getInputStream() throws Exception {
        return clz.getResourceAsStream(resource);
    }
}