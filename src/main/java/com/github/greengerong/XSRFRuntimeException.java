package com.github.greengerong;


public class XSRFRuntimeException extends RuntimeException {
    public XSRFRuntimeException(String message) {
        super(message);
    }

    public XSRFRuntimeException() {
    }


    public XSRFRuntimeException(java.lang.String message, java.lang.Throwable throwable) {
        super(message, throwable);
    }

    public XSRFRuntimeException(java.lang.Throwable throwable) {
        super(throwable);
    }
}
