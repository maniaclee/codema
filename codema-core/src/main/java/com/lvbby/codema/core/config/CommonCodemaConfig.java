package com.lvbby.codema.core.config;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.CodemaMachine;
import com.lvbby.codema.core.ResultContext;
import com.lvbby.codema.core.ResultHandler;
import com.lvbby.codema.core.result.Result;
import com.lvbby.codema.core.utils.FileUtils;
import com.lvbby.codema.core.utils.ReflectionUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lipeng on 2016/12/22.
 */
public class CommonCodemaConfig implements Serializable, ResultHandler {
    static Logger logger = LoggerFactory.getLogger(CommonCodemaConfig.class);
    private String author = System.getProperty("user.name");
    /**
     * 最终输出文件的根目录,必须存在，支持~ / ../ ./
     */
    private String destRootDir;
    private List<ResultHandler> resultHandlers = Lists.newLinkedList();

    private boolean inited = false;

    public <T extends CommonCodemaConfig> T copy(Class<T> targetConfigClass){
        T re = ReflectionUtils.instance(targetConfigClass);
        try {
            BeanUtils.copyProperties(re, this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return re;
    }

    /**
     * 初始化
     */
    public void init() {
        if (inited) {
            return;
        }
//        if (StringUtils.isNotBlank(destRootDir)) {
//            //转化为标准路径
//            File file = FileUtils.parseFileRoot(destRootDir);
//            Validate.isTrue(file.exists(), String.format("dest root dir not exist:%s", destRootDir));
//            destRootDir = file.getAbsolutePath();
//        }
        inited = true;
    }

    protected String parseFileWithParent(String parent, String sub, String message) {
        return parseFileWithParent(parent, sub, message, false);
    }

    /***
     * 根据parent解析file
     * 1. parent 必须存在,如果不存在返回null
     * 2. 如果sub是绝对路径，返回sub
     * @param parent
     * @param sub
     * @param message
     * @return
     */
    protected String parseFileWithParent(String parent, String sub, String message, boolean parentMustExist) {
        if (StringUtils.isBlank(parent)) {
            if (StringUtils.isNotBlank(sub)) {
                logger.warn("{}parent is null,ignore path {}", message, sub);
            }
            return null;
        }
        if (StringUtils.isBlank(sub)) {
            logger.warn("{} ignore blank path", message);
            return null;
        }
        Validate.notBlank(sub, "[%s]child file can't be empty,parent dir: %s", message, sub);
        if (parentMustExist) {
            Validate.isTrue(FileUtils.exist(parent), "[%s]parent file not exist:%s".format(message, parent));
        }
        //解析子路径
        String subFile = FileUtils.parseFileRoot(sub).getPath();
        if (!FileUtils.isRelativeFilePath(subFile)) {
            Validate.isTrue(FileUtils.exist(subFile), "[%s]path not exist: %s", message, sub);
            return subFile;
        }
        return FileUtils.parseFile(parent, subFile).getAbsolutePath();
    }

    @Override
    public void handle(ResultContext resultContext) throws Exception {
        for (ResultHandler handler : resultHandlers) {
            handler.handle(resultContext);
        }
    }

    public void handle(CodemaContext codemaContext, Result result) throws Exception {
        handle(ResultContext.of(codemaContext, this, result));
    }

    public CodemaMachine loadCodemaMachine() {
        ConfigBind annotation = getClass().getAnnotation(ConfigBind.class);
        if (annotation != null && annotation.value() != null
            && CodemaMachine.class.isAssignableFrom(annotation.value())) {
            return ReflectionUtils.instance(annotation.value());
        }
        return null;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    public CommonCodemaConfig addResultHandler(Class<? extends ResultHandler> resultHandler) {
        this.resultHandlers.add(ReflectionUtils.instance(resultHandler));
        return this;
    }

    public List<ResultHandler> getResultHandlers() {
        return resultHandlers;
    }

    public void setResultHandlers(List<ResultHandler> resultHandlers) {
        this.resultHandlers = resultHandlers;
    }

    public String getDestRootDir() {
        return destRootDir;
    }

    public void setDestRootDir(String destRootDir) {
        this.destRootDir = destRootDir;
    }
}
