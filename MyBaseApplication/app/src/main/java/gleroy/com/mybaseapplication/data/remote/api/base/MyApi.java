package gleroy.com.mybaseapplication.data.remote.api.base;

import gleroy.com.mybaseapplication.data.remote.entity.MyRemoteObject;
import retrofit2.Call;
import retrofit2.http.GET;

public interface MyApi {

    /*----- GET requests -----*/

    @GET("/google.com")
    Call<MyRemoteObject> getSomethingFromApi();
}
