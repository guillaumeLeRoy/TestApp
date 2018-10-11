package gleroy.com.mybaseapplication.presentation.viewmodel

import android.app.Application
import gleroy.com.mybaseapplication.data.local.entity.TodoEntity
import gleroy.com.mybaseapplication.data.remote.api.parameter.GetTodosRequestParam
import gleroy.com.mybaseapplication.data.remote.entity.Todo
import gleroy.com.mybaseapplication.data.repository.TodoRepository
import gleroy.com.mybaseapplication.data.request.RequestParameter
import gleroy.com.mybaseapplication.presentation.viewmodel.base.ADataLoaderViewModel
import timber.log.Timber
import javax.inject.Inject

class MyViewModel @Inject constructor(application: Application, repository: TodoRepository) : ADataLoaderViewModel<TodoEntity>(application, repository) {

    override fun createFetchParameter(forceRefresh: Boolean): RequestParameter {
        Timber.d("createFetchParameter")
        return GetTodosRequestParam()
    }

    init {
        changeToMainState()
    }
}