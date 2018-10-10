package gleroy.com.mybaseapplication.data.remote.api.base

import gleroy.com.mybaseapplication.data.remote.entity.MyRemoteObject
import retrofit2.Call
import retrofit2.http.GET

interface MyApi {

    /*----- GET requests -----*/

    @get:GET("/todos/1")
    val somethingFromApi: Call<MyRemoteObject>
}
