package gleroy.com.mybaseapplication.data.remote.api.base.error;

import android.support.annotation.Nullable;

/**
 * Exception raised when failing to parse error response
 */
public class ErrorParsingException extends AJobException {

    @Nullable
    private String mReceivedError;

    public ErrorParsingException(@Nullable Integer code, @Nullable String method, @Nullable String url, @Nullable String error) {
        super(code, method, url);
        mReceivedError = error;
    }

}
