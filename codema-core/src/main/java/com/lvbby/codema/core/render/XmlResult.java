package com.lvbby.codema.core.render;

import com.lvbby.codema.core.ResultContext;
import com.lvbby.codema.core.result.BasicResult;
import com.lvbby.codema.core.result.MergeCapableFileResult;
import com.lvbby.codema.core.utils.XmlMerger;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;

/**
 *
 * @author dushang.lp
 * @version $Id: XmlResult.java, v 0.1 2017-09-18 上午10:31 dushang.lp Exp $
 */
public class XmlResult extends BasicResult implements MergeCapableFileResult {
    @Override
    public String parseMergeResult(InputStream dest, ResultContext resultContext) throws Exception {
        return new XmlMerger().merge(getString(), IOUtils.toString(dest));
    }
}