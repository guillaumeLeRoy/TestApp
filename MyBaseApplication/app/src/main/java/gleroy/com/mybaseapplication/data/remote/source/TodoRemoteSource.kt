package gleroy.com.mybaseapplication.data.remote.source

import gleroy.com.mybaseapplication.data.remote.api.request.GetTodosApiRequest
import gleroy.com.mybaseapplication.data.remote.api.base.BaseApiRequest
import gleroy.com.mybaseapplication.data.remote.api.parameter.GetTodosRequestParam
import gleroy.com.mybaseapplication.data.remote.base.RequestUtils
import gleroy.com.mybaseapplication.data.remote.entity.Todo
import gleroy.com.mybaseapplication.data.request.RequestParameter
import gleroy.com.mybaseapplication.utils.react.BaseSchedulerProvider
import io.reactivex.Maybe
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject

class TodoRemoteSource @Inject constructor(private val schedulerProvider: BaseSchedulerProvider, private val requestUtils: RequestUtils) {

    fun get(parameter: RequestParameter): Maybe<Todo> {
        Timber.d("get")
        return Maybe.error(Throwable("Unsupported param $parameter"))
    }

    fun getAll(parameter: RequestParameter): Single<List<Todo>> {
        Timber.d("getAll")
        if (parameter is GetTodosRequestParam) {
            val request = GetTodosApiRequest(parameter, requestUtils)
            return executeRequest(request).toSingle()
        }
        return Single.error(Throwable("Unsupported param $parameter"))
    }

    private fun <Result> executeRequest(req: BaseApiRequest<Result>): Maybe<Result> {
        val response = req.getResponse()
        return response
                .doOnSubscribe { req.execute() }
                .observeOn(schedulerProvider.ui())
                .subscribeOn(schedulerProvider.io())
    }
}