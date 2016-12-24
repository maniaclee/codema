package com.lvbby.codema.core.common;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.CodemaException;
import com.lvbby.codema.core.TypedCodeMachine;
import com.lvbby.codema.core.config.CoderCommonConfig;
import org.apache.commons.lang3.Validate;

import java.net.URI;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Created by lipeng on 2016/12/24.
 * wrap SourceParser to do the job
 */
public class CodemaCommonMachine extends TypedCodeMachine<CoderCommonConfig> {

    private List<SourceParser> sourceParsers;

    public CodemaCommonMachine() {
        this.sourceParsers = loadSourceParsers();
    }

    /***
     * 添加自定义的uri协议解析
     */
    public CodemaCommonMachine addSourceParser(SourceParser sourceParser) {
        sourceParsers.add(sourceParser);
        return this;
    }

    @Override
    public void code(CodemaContext codemaContext, CoderCommonConfig config) throws Exception {
        Validate.notBlank(config.getFrom(), "from can't be empty");
        getSourceParser(config.getFrom()).code(codemaContext);
    }

    private SourceParser getSourceParser(String from) throws CodemaException {
        String scheme = URI.create(from).getScheme();
        return sourceParsers.stream().filter(sourceParser -> from.startsWith(sourceParser.getSupportedUriScheme())).findFirst().orElseThrow(() -> new CodemaException("unknown source type: " + scheme));
    }

    private List<SourceParser> loadSourceParsers() {
        return Lists.newArrayList(ServiceLoader.load(SourceParser.class));
    }
}
