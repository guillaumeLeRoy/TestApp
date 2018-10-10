package gleroy.com.mybaseapplication.data.remote.source

import gleroy.com.mybaseapplication.data.remote.api.GetSomethingFromApi
import gleroy.com.mybaseapplication.data.remote.api.base.BaseApiRequest
import gleroy.com.mybaseapplication.data.remote.base.RequestUtils
import gleroy.com.mybaseapplication.data.remote.entity.MyRemoteObject
import io.reactivex.Maybe
import timber.log.Timber
import javax.inject.Inject

class MyRemoteSource @Inject constructor(private val requestUtils: RequestUtils) {

    fun get(): Maybe<MyRemoteObject> {
        Timber.d("get")

        val apiReq = GetSomethingFromApi(requestUtils)
        executeRequest(apiReq)

        return Maybe.just(MyRemoteObject())
    }

    private fun <Result> executeRequest(req: BaseApiRequest<Result>) {
        req.execute()
    }
}