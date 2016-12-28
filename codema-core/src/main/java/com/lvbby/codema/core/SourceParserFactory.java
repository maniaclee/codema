package com.lvbby.codema.core;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by lipeng on 16/12/28.
 */
public class SourceParserFactory {
    private List<SourceParser> sourceParsers = Lists.newLinkedList();

    public static SourceParserFactory of(List<SourceParser> sourceParsers) {
        return new SourceParserFactory(sourceParsers);
    }

    private SourceParserFactory(List<SourceParser> sourceParsers) {
        if (sourceParsers != null)
            this.sourceParsers = sourceParsers;
    }

    public SourceParser load(String s) {
        return this.sourceParsers.stream().filter(sourceParser -> s.startsWith(sourceParser.getSupportedUriScheme())).findFirst().orElse(null);
    }

    public List<SourceParser> getSourceParsers() {
        return sourceParsers;
    }

    public void setSourceParsers(List<SourceParser> sourceParsers) {
        this.sourceParsers = sourceParsers;
    }
}
