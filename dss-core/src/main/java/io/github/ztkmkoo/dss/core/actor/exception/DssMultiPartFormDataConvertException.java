package io.github.ztkmkoo.dss.core.actor.exception;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 6. 18. 오전 1:30
 */
public class DssMultiPartFormDataConvertException extends RuntimeException {

    public DssMultiPartFormDataConvertException() {
        super();
    }

    public DssMultiPartFormDataConvertException(String s) {
        super(s);
    }

    public DssMultiPartFormDataConvertException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public DssMultiPartFormDataConvertException(Throwable throwable) {
        super(throwable);
    }

    protected DssMultiPartFormDataConvertException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
