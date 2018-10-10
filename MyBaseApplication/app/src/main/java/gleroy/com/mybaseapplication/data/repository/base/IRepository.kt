package gleroy.com.mybaseapplication.data.repository.base

import gleroy.com.mybaseapplication.data.request.RequestParameter
import io.reactivex.Maybe
import io.reactivex.Single

interface IRepository<T> {

    fun fetch(param: RequestParameter): Maybe<T>

    fun fetchAll(param: RequestParameter): Single<List<T>>

}