package gleroy.com.mybaseapplication.data.remote.source

import gleroy.com.mybaseapplication.data.remote.api.GetSomethingFromApi
import gleroy.com.mybaseapplication.data.remote.api.base.BaseApiRequest
import gleroy.com.mybaseapplication.data.remote.api.parameter.MyRequestParam
import gleroy.com.mybaseapplication.data.remote.base.RequestUtils
import gleroy.com.mybaseapplication.data.remote.entity.MyRemoteObject
import gleroy.com.mybaseapplication.data.request.RequestParameter
import gleroy.com.mybaseapplication.utils.react.BaseSchedulerProvider
import io.reactivex.Maybe
import timber.log.Timber
import javax.inject.Inject

class MyRemoteSource @Inject constructor(private val schedulerProvider: BaseSchedulerProvider, private val requestUtils: RequestUtils) {

    fun get(parameter: RequestParameter): Maybe<MyRemoteObject> {
        Timber.d("get")
        if (parameter is MyRequestParam) {
            val request = GetSomethingFromApi(parameter, requestUtils)
            return executeRequest(request)
        }
        return Maybe.error(Throwable("Unsupported param $parameter"))
    }

    private fun <Result> executeRequest(req: BaseApiRequest<Result>): Maybe<Result> {
        val response = req.getResponse()
        return response
                .doOnSubscribe { req.execute() }
                .observeOn(schedulerProvider.ui())
                .subscribeOn(schedulerProvider.io())
    }
}