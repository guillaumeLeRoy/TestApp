package gleroy.com.mybaseapplication.data.remote.api.base.error;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

/**
 */
public class ErrorsModel implements Serializable {

    @NonNull
    private List<ErrorModel> mErrors;

    public ErrorsModel(@NonNull List<ErrorModel> errors) {
        mErrors = errors;
    }

    @NonNull
    public List<ErrorModel> getErrors() {
        return mErrors;
    }

}
