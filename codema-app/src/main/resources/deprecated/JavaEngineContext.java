package deprecated;

/**
 * Created by lipeng on 2016/12/28.
 */
public class JavaEngineContext {
    private String fromClassName;


    public String getFromClassName() {
        return fromClassName;
    }

    public void setFromClassName(String fromClassName) {
        this.fromClassName = fromClassName;
    }

    public JavaEngineContext className(String fromClassName) {
        this.fromClassName = fromClassName;
        return this;
    }
}
