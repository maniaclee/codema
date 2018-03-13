package com.lvbby.codema.java.tool;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.config.NotBlank;
import com.lvbby.codema.core.utils.FileUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Created by lipeng on 16/12/23.
 */
@Data
@NoArgsConstructor
public class MavenConfig {
    @NotBlank
    private String artifactId;
    @NotBlank
    private String groupId;
    @NotBlank
    private String destRootDir;

    private String version     = "1.0";
    private String javaVersion = "1.8";
    /**
     * 目标src路径
     */
    private String destSrcRoot;
    private String destResourceRoot;

    private String destTestSrcRoot;
    private String destTestResourceRoot;

    private List<MavenConfig> modules;
    private MavenConfig       parent;

    public static MavenConfig create(String destRootDir, String artifactId, String groupId) {
        return new MavenConfig(destRootDir, artifactId, groupId);
    }

    /***
     * 解析某个maven项目路径，只构造，不扫描
     * @param destRootDir
     * @return
     */
    public static MavenConfig parse(String destRootDir) {
        Validate.notBlank(destRootDir);
        File file = new File(destRootDir);
        Validate.isTrue(isMavenDirectory(file));
        File pom = new File(file, "pom.xml");
        Document doc = null;
        try {
            doc = new SAXReader().read(new FileInputStream(pom));
        } catch (Exception e) {
            throw new RuntimeException(String.format("error parsing maven:%s", destRootDir),e);
        }
        Element root = doc.getRootElement();
        Validate.notNull(root,"非法pom.xml:没有根元素");

        MavenConfig mavenConfig = new MavenConfig(destRootDir,
                Optional.ofNullable(root.element("artifactId")).map(Element::getText).orElse(null),
                Optional.ofNullable(root.element("groupId")).map(Element::getText).orElse(null));
        mavenConfig.setVersion(root.element("version").getText());
        return mavenConfig;
    }

    private MavenConfig(String destRootDir, String artifactId, String groupId) {
        Validate.isTrue(!FileUtils.isRelativeFilePath(destRootDir));
        this.artifactId = artifactId;
        this.groupId = groupId;
        this.destRootDir = destRootDir;
        Function<String, String> f = s -> new File(destRootDir, s).getAbsolutePath();
        setDestSrcRoot(f.apply("src/main/java"));
        setDestResourceRoot(f.apply("src/main/resources"));
        setDestTestResourceRoot(f.apply("src/test/resources"));
        setDestTestSrcRoot(f.apply("src/test/java"));
    }

    private MavenConfig addChild(MavenConfig mavenConfig) {
        if (modules == null)
            modules = Lists.newLinkedList();
        modules.add(mavenConfig);
        mavenConfig.parent = this;
        return this;
    }

    /***
     * 把树形的maven转化为List
     * @return
     */
    public List<MavenConfig> toList(){
        List<MavenConfig> re = Lists.newLinkedList();
        re.add(this);
        if(CollectionUtils.isNotEmpty(modules)){
            for (MavenConfig module : modules) {
                List<MavenConfig> mavenConfigs = module.toList();
                if(CollectionUtils.isNotEmpty(mavenConfigs)){
                    re.addAll(mavenConfigs);
                }
            }
        }
        return re;
    }

    public void scanChild() {
        scanChild(-1);
    }

    public void scanChild(int maxDepth) {
        scanChild(1, maxDepth);
    }

    private void scanChild(int depth, int maxDepth) {
        if (maxDepth < 0 || depth <= maxDepth) {
            Arrays.stream(new File(destRootDir).listFiles()).filter(file -> isMavenDirectory(file)).forEach(file -> {
                MavenConfig mvn = null;
                try {
                    mvn = parse(file.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                addChild(mvn);
                mvn.scanChild(depth + 1, maxDepth);
            });
        }
    }

    public static List<MavenConfig> scan(List<File> roots) {
        return scan(roots, 5);
    }

    public static List<MavenConfig> scan(List<File> roots, int maxDepth) {
        List<MavenConfig> result = Lists.newLinkedList();
        roots.forEach(file -> scanMaven(file, result, 1, maxDepth));
        return result;
    }

    private static boolean isMavenDirectory(File file) {
        return file != null && file.isDirectory() && file.exists() && new File(file, "pom.xml").exists();
    }

    private static void scanMaven(File file, List<MavenConfig> result, int depth, int maxDepth) {
        if (maxDepth < 0 || depth <= maxDepth) {
            if (isMavenDirectory(file)) {
                MavenConfig mvn = null;
                try {
                    mvn = parse(file.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                mvn.scanChild(depth + 1, maxDepth);
                result.add(mvn);
                return;
            }
            if (depth < maxDepth) {
                Arrays.stream(file.listFiles()).filter(f -> f.isDirectory()).forEach(f -> scanMaven(f, result, depth + 1, maxDepth));
            }
        }
    }

}
