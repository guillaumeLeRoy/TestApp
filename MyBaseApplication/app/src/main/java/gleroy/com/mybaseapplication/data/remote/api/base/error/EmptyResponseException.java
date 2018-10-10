package gleroy.com.mybaseapplication.data.remote.api.base.error;

import android.support.annotation.Nullable;

/**
 * Exception raised when the response is empty
 */
public class EmptyResponseException extends AJobException {

    public EmptyResponseException(@Nullable Integer code, @Nullable String method, @Nullable String url) {
        super(code, method, url);
    }
}
