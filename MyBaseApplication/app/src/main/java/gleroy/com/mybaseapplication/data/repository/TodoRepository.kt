package gleroy.com.mybaseapplication.data.repository

import gleroy.com.mybaseapplication.data.local.entity.TodoEntity
import gleroy.com.mybaseapplication.data.local.source.TodoLocalSource
import gleroy.com.mybaseapplication.data.remote.entity.Todo
import gleroy.com.mybaseapplication.data.remote.source.TodoRemoteSource
import gleroy.com.mybaseapplication.data.repository.base.IRepository
import gleroy.com.mybaseapplication.data.request.RequestParameter
import io.reactivex.Maybe
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepository @Inject constructor(private val remoteSource: TodoRemoteSource,
                                         private val localSource: TodoLocalSource) : IRepository<Todo> {

    override fun fetch(param: RequestParameter): Maybe<Todo> {
        return remoteSource.get(param)
    }

    override fun fetchAll(param: RequestParameter): Single<List<Todo>> {
        localSource.getAll()
                .map { fromCache: List<TodoEntity> -> fromCache.isEmpty() }
        return remoteSource.getAll(param)
    }


}