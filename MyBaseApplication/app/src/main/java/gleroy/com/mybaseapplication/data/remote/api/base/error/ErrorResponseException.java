package gleroy.com.mybaseapplication.data.remote.api.base.error;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class ErrorResponseException extends AJobException {

    /**
     * API error
     */
    @NonNull
    private ErrorsModel mError;

    public ErrorResponseException(@Nullable Integer code, @Nullable String method, @Nullable String url, @NonNull ErrorsModel error) {
        super(code, method, url);
        mError = error;
    }

    @NonNull
    public ErrorsModel getAPIerror() {
        return mError;
    }

}
