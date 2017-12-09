package com.lvbby.codema.core.handler;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.result.PrintableResult;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 将输出内容汇总，最后一起处理
 * Created by lipeng on 2016/12/26.
 */
public class PrintCollectResultHandler extends AbstractResultHandler<PrintableResult> {

    private List<String> resultList = Lists.newLinkedList();

    @Override protected void process(PrintableResult result) {
        resultList.add(result.getString());
    }

    public List<String> getStringList() {
        return resultList;
    }

    public String getString() {
        return resultList.stream().collect(Collectors.joining("\n"));
    }
}
