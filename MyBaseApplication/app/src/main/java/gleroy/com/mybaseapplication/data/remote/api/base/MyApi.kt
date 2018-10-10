package gleroy.com.mybaseapplication.data.remote.api.base

import gleroy.com.mybaseapplication.data.remote.entity.Todo
import retrofit2.Call
import retrofit2.http.GET

interface MyApi {

    /*----- GET requests -----*/

    @get:GET("/todos")
    val getTodos: Call<List<Todo>>
}
