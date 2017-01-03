package com.lvbby.codema.core.error;

/**
 * Created by lipeng on 16/12/23.
 */
public class CodemaRuntimeException extends RuntimeException {
    public CodemaRuntimeException(String message) {
        super(message);
    }

    public CodemaRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
