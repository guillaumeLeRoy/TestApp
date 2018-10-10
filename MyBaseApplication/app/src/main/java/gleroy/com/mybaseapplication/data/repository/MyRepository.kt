package gleroy.com.mybaseapplication.data.repository

import gleroy.com.mybaseapplication.data.local.MyLocalSource
import gleroy.com.mybaseapplication.data.local.entity.MyLocalObject
import gleroy.com.mybaseapplication.data.remote.entity.Todo
import gleroy.com.mybaseapplication.data.remote.source.MyRemoteSource
import gleroy.com.mybaseapplication.data.repository.base.IRepository
import gleroy.com.mybaseapplication.data.request.RequestParameter
import io.reactivex.Maybe
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyRepository @Inject constructor(private val remoteSource: MyRemoteSource,
                                       private val localSource: MyLocalSource) : IRepository<Todo> {

    override fun fetch(param: RequestParameter): Maybe<Todo> {
        return remoteSource.get(param)
    }

    override fun fetchAll(param: RequestParameter): Single<List<Todo>> {
        return remoteSource.getAll(param)
    }


}