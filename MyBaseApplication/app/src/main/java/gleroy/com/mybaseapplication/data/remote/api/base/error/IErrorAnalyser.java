package gleroy.com.mybaseapplication.data.remote.api.base.error;

import android.support.annotation.NonNull;

import java.util.List;

/**
 */
public interface IErrorAnalyser {

    boolean handleAPIerror(@NonNull List<ErrorModel> errors);
}
