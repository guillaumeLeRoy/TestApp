package gleroy.com.mybaseapplication.data.remote.api

import gleroy.com.mybaseapplication.data.remote.api.base.BaseApiRequest
import gleroy.com.mybaseapplication.data.remote.base.RequestUtils
import gleroy.com.mybaseapplication.data.remote.base.RequestsGenerator
import gleroy.com.mybaseapplication.data.remote.entity.MyRemoteObject
import retrofit2.Call

class GetSomethingFromApi(requestUtils: RequestUtils) : BaseApiRequest<MyRemoteObject>(requestUtils) {

    override fun executeRequest(requestsGenerator: RequestsGenerator): Call<MyRemoteObject> {
        return requestsGenerator.api.somethingFromApi
    }

}