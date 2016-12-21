package com.lvbby.codema.coder;

/**
 * Created by lipeng on 16/12/21.
 */
public class CoderException extends Exception {

    public CoderException(String message, Throwable cause) {
        super(message, cause);
    }

    public CoderException(String message) {
        super(message);
    }
}
