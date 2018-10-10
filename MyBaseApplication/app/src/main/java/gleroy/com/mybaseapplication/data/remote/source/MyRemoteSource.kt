package gleroy.com.mybaseapplication.data.remote.source

import gleroy.com.mybaseapplication.data.remote.api.GetSomethingFromApi
import gleroy.com.mybaseapplication.data.remote.api.base.BaseApiRequest
import gleroy.com.mybaseapplication.data.remote.api.parameter.MyRequestParam
import gleroy.com.mybaseapplication.data.remote.base.RequestUtils
import gleroy.com.mybaseapplication.data.remote.entity.MyRemoteObject
import gleroy.com.mybaseapplication.data.request.RequestParameter
import io.reactivex.Maybe
import timber.log.Timber
import javax.inject.Inject

class MyRemoteSource @Inject constructor(private val requestUtils: RequestUtils) {

    fun get(parameter: RequestParameter): Maybe<MyRemoteObject> {
        Timber.d("get")
        if (parameter is MyRequestParam) {
            // todo exec # thread
            Maybe.fromCallable {  val apiReq = GetSomethingFromApi(parameter, requestUtils)
                executeRequest(apiReq) }

            return Maybe.just(MyRemoteObject())
        }
        return Maybe.error(Throwable("Unsupported param $parameter"))
    }

    private fun <Result> executeRequest(req: BaseApiRequest<Result>) {
        req.execute()
    }
}