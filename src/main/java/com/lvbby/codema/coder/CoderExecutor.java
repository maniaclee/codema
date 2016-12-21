package com.lvbby.codema.coder;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 2016/12/20.
 */
public class CoderExecutor {

    public void exec(CoderBaseRequest request, CoderHandler... coderHandlers) throws Exception {
        if (coderHandlers != null)
            exec(request, Lists.newArrayList(coderHandlers));
    }

    public void exec(CoderBaseRequest request, List<CoderHandler> coderHandlers) throws Exception {
        if (CollectionUtils.isNotEmpty(coderHandlers))
            for (CoderHandler coderHandler : coderHandlers) {
                coderHandler.handle(request);
            }
    }

    public void execClass(CoderBaseRequest request, Class<? extends CoderHandler>... coderHandlers) throws Exception {
        exec(request,
                Lists.newArrayList(coderHandlers).stream().map(clz -> {
                    try {
                        return clz.newInstance();
                    } catch (Exception e) {
                        return null;
                    }
                }).filter(e -> e != null).collect(Collectors.toList()));
    }
}
