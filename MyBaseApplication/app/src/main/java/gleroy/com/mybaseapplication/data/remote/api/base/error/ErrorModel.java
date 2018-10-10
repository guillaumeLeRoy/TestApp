package gleroy.com.mybaseapplication.data.remote.api.base.error;

import android.support.annotation.Nullable;

import java.io.Serializable;

public class ErrorModel implements Serializable {

    @Nullable
    private String mCode;
    @Nullable
    private String mMessage;

    public ErrorModel() {
    }

    public ErrorModel(@Nullable String code, @Nullable String message) {
        mCode = code;
        mMessage = message;
    }

    @Nullable
    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
    }

    @Nullable
    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }


}
