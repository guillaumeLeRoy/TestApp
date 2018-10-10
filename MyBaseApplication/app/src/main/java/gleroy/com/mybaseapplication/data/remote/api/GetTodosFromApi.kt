package gleroy.com.mybaseapplication.data.remote.api

import gleroy.com.mybaseapplication.data.remote.api.base.BaseApiRequest
import gleroy.com.mybaseapplication.data.remote.api.parameter.GetTodosRequestParam
import gleroy.com.mybaseapplication.data.remote.base.RequestUtils
import gleroy.com.mybaseapplication.data.remote.base.RequestsGenerator
import gleroy.com.mybaseapplication.data.remote.entity.Todo
import retrofit2.Call

class GetTodosFromApi(param: GetTodosRequestParam, requestUtils: RequestUtils) : BaseApiRequest<List<Todo>>(requestUtils) {

    override fun executeRequest(requestsGenerator: RequestsGenerator): Call<List<Todo>> {
        return requestsGenerator.api.getTodos
    }

}