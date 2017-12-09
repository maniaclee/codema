package com.lvbby.codema.core.handler;

import com.lvbby.codema.core.result.PrintableResult;

/**
 * Created by lipeng on 2016/12/26.
 */
public class PrintResultHandler extends AbstractResultHandler<PrintableResult> {

    @Override protected void process(PrintableResult result) {
        System.out.println(result.getString());
    }
}
