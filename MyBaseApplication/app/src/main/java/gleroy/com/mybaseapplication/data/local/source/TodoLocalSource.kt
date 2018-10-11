package gleroy.com.mybaseapplication.data.local.source

import gleroy.com.mybaseapplication.data.local.dao.TodoDao
import gleroy.com.mybaseapplication.data.local.entity.TodoEntity
import io.reactivex.Single
import javax.inject.Inject

class TodoLocalSource @Inject constructor(private val todoDao: TodoDao) {

    fun getAll(): Single<List<TodoEntity>> {
        return todoDao.getAll()
    }

}