package com.lvbby.codema.core.render;

import com.lvbby.codema.core.ResultContext;
import com.lvbby.codema.core.result.MergeCapableFileResult;

import java.io.InputStream;
import java.util.Map;

/**
 * Created by lipeng on 2017/1/14.
 */
public class XmlTemplateResult extends TemplateEngineResult implements MergeCapableFileResult{

    @Override
    protected void beforeRender(Map bindingParameters) {
        template(getTemplate().replaceAll("<!--\\s*", "").replaceAll("\\s*-->", ""));
    }

    @Override
    public String parseMergeResult(InputStream dest, ResultContext resultContext) throws Exception {
        return null;
    }
}
