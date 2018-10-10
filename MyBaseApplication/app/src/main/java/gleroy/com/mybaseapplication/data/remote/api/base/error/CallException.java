package gleroy.com.mybaseapplication.data.remote.api.base.error;

import android.support.annotation.Nullable;

/**
 * Exception raised when something unexpected happened during the execution
 */
public class CallException extends AJobException {

    @Nullable
    private Throwable mGeneratedException;

    public CallException(@Nullable Throwable exception, @Nullable Integer code, @Nullable String method, @Nullable String url) {
        super(code, method, url);
        mGeneratedException = exception;
    }

}
