package gleroy.com.mybaseapplication.data.remote.api.base.error;

import android.support.annotation.Nullable;


/**
 * Exception raised when the parsing of the successful response failed
 */
public class SuccessParsingException extends AJobException {

    public SuccessParsingException(@Nullable Integer code, @Nullable String method, @Nullable String url) {
        super(code, method, url);
    }


}
