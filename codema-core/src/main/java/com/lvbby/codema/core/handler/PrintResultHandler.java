package com.lvbby.codema.core.handler;

import com.lvbby.codema.core.result.PrintableResult;

/**
 * Created by lipeng on 2016/12/26.
 */
public class PrintResultHandler extends AbstractResultHandler<PrintableResult> {
    private final String format_normal = "format_normal";
    private final String format_json   = "json";
    private final String format_json_pretty   = "";

    @Override protected void process(PrintableResult result) {
        System.out.println(result.getString());
    }
}
