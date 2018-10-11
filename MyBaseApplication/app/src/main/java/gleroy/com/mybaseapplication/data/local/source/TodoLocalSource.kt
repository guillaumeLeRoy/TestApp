package gleroy.com.mybaseapplication.data.local.source

import gleroy.com.mybaseapplication.data.local.dao.TodoDao
import gleroy.com.mybaseapplication.data.local.entity.TodoEntity
import gleroy.com.mybaseapplication.data.remote.entity.Todo
import gleroy.com.mybaseapplication.data.request.RequestParameter
import gleroy.com.mybaseapplication.utils.react.BaseSchedulerProvider
import io.reactivex.Completable
import io.reactivex.Single
import org.simpleframework.xml.core.Complete
import javax.inject.Inject

class TodoLocalSource @Inject constructor(private val todoDao: TodoDao, private val schedulerProvider: BaseSchedulerProvider) {

    fun getAll(param: RequestParameter): Single<List<TodoEntity>> {
        return todoDao.getAll()
                .observeOn(schedulerProvider.ui())
                .subscribeOn(schedulerProvider.io())
    }

    fun save(param: RequestParameter, todos: List<TodoEntity>): Completable {
        return Completable.fromAction { todoDao.addAll(todos) }
                .observeOn(schedulerProvider.ui())
                .subscribeOn(schedulerProvider.io())

    }

}