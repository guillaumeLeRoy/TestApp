package gleroy.com.mybaseapplication.data.remote.api.base.error;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import timber.log.Timber;

public abstract class AJobException extends RuntimeException {

    @Nullable
    private Integer mStatusCode;
    @Nullable
    private String mMethod;
    @Nullable
    private String mUrl;

    public AJobException(@Nullable Integer code, @Nullable String method, @Nullable String url) {
        mStatusCode = code;
        mMethod = method;
        mUrl = url;
        Timber.d("mStatusCode : " + mStatusCode + ", mMethod : " + mMethod + ", url : " + mUrl);
    }

    @Nullable
    public String getMethod() {
        return mMethod;
    }

    @Nullable
    public String getUrl() {
        return mUrl;
    }

    @Nullable
    public Integer getStatusCode() {
        return mStatusCode;
    }

    @NonNull
    public String getLogLabels() {
        StringBuilder strb = new StringBuilder();

        if (mStatusCode != null) {
            if (strb.length() != 0) {
                strb.append("|");
            }

            strb.append("code:").append(mStatusCode);
        }

        if (mMethod != null) {
            if (strb.length() != 0) {
                strb.append("|");
            }
            strb.append("method:").append(mMethod);
        }

        if (mUrl != null) {
            if (strb.length() != 0) {
                strb.append("|");
            }
            strb.append("url:").append(mUrl);
        }

        return strb.toString();
    }

}
