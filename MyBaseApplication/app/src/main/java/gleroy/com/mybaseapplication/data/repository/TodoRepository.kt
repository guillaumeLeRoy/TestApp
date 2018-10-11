package gleroy.com.mybaseapplication.data.repository

import gleroy.com.mybaseapplication.data.local.entity.TodoEntity
import gleroy.com.mybaseapplication.data.local.source.TodoLocalSource
import gleroy.com.mybaseapplication.data.remote.entity.Todo
import gleroy.com.mybaseapplication.data.remote.source.TodoRemoteSource
import gleroy.com.mybaseapplication.data.repository.base.IRepository
import gleroy.com.mybaseapplication.data.request.RequestParameter
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepository @Inject constructor(private val remoteSource: TodoRemoteSource,
                                         private val localSource: TodoLocalSource) : IRepository<TodoEntity> {

    override fun fetch(param: RequestParameter): Maybe<TodoEntity> {
        return remoteSource.get(param).map { todo: Todo -> map(todo) }
    }

    override fun fetchAll(param: RequestParameter): Single<List<TodoEntity>> {
        return getAllFromCache(param)
                .flatMap { fromCache: List<TodoEntity> ->
                    if (fromCache.isEmpty()) {
                        return@flatMap fetchFromRemoteAndSaveIntoCache(param)
                    }
                    return@flatMap Single.just(fromCache)
                }
    }

    private fun fetchFromRemoteAndSaveIntoCache(param: RequestParameter): Single<List<TodoEntity>> {
        return remoteSource.getAll(param)
                .flatMapCompletable { remote: List<Todo> -> saveAllIntoCache(param, remote) }
                .andThen(getAllFromCache(param))
    }

    private fun getAllFromCache(param: RequestParameter): Single<List<TodoEntity>> {
        return localSource.getAll(param)
    }

    private fun saveAllIntoCache(param: RequestParameter, todos: List<Todo>): Completable {
        return localSource.save(param, map(todos))
    }

    private fun map(from: Todo): TodoEntity {
        return TodoEntity(from.id,
                from.userId,
                from.title,
                from.completed)
    }

    private fun map(from: List<Todo>): List<TodoEntity> {
        return from.map { map(it) }
    }

}