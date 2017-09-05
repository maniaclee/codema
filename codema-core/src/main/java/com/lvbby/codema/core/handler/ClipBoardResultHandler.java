package com.lvbby.codema.core.handler;

import com.lvbby.codema.core.ResultContext;
import com.lvbby.codema.core.ResultHandler;
import com.lvbby.codema.core.result.PrintableResult;
import com.lvbby.codema.core.result.Result;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

/**
 * Created by lipeng on 2017/9/5.
 */
public class ClipBoardResultHandler implements ResultHandler {
    @Override
    public void handle(ResultContext resultContext) throws Exception {
        Result result = resultContext.getResult();
        if (result instanceof PrintableResult) {
            Clipboard sysClb = null;
            sysClb = Toolkit.getDefaultToolkit().getSystemClipboard();
            sysClb.setContents(new StringSelection(((PrintableResult) result).getString()), null);
        }
    }
}
