package com.lvbby.codema.core.resource;

/**
 *
 * @author dushang.lp
 * @version $Id: AbstractNamedResource.java, v 0.1 2017-11-09 下午11:36 dushang.lp Exp $$
 */
public abstract class AbstractNamedResource implements NamedResource{
    private String resourceName;

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    @Override public String getResourceName() {
        return resourceName;
    }
}