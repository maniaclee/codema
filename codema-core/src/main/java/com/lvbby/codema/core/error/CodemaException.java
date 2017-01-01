package com.lvbby.codema.core.error;

/**
 * Created by lipeng on 16/12/23.
 */
public class CodemaException extends Exception {
    public CodemaException(String message) {
        super(message);
    }

    public CodemaException(String message, Throwable cause) {
        super(message, cause);
    }
}
