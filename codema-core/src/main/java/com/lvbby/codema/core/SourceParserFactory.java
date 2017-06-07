package com.lvbby.codema.core;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.utils.CodemaUtils;

import java.net.URI;
import java.util.List;

/**
 * Created by lipeng on 16/12/28.
 */
public class SourceParserFactory {
    private List<SourceParser> sourceParsers = Lists.newLinkedList();

    public static SourceParserFactory of(List<SourceParser> sourceParsers) {
        return new SourceParserFactory(sourceParsers);
    }

    public static SourceParserFactory of(ClassLoader classLoader) {
        return new SourceParserFactory(CodemaUtils.loadService(SourceParser.class, classLoader));
    }

    public static SourceParserFactory defaultInstance() {
        return of(CodemaUtils.loadService(SourceParser.class));
    }

    private SourceParserFactory(List<SourceParser> sourceParsers) {
        if (sourceParsers != null)
            this.sourceParsers = sourceParsers;
    }

    public SourceParser load(String s) {
        return this.sourceParsers.stream().filter(sourceParser -> s.startsWith(sourceParser.getSupportedUriScheme())).findFirst().orElse(null);
    }

    /***
     * quick method to parse uri
     * @param uri
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> T parse(String uri) throws Exception {
        return (T) load(uri).parse(URI.create(uri));
    }

    public List<SourceParser> getSourceParsers() {
        return sourceParsers;
    }

    public void setSourceParsers(List<SourceParser> sourceParsers) {
        this.sourceParsers = sourceParsers;
    }
}
