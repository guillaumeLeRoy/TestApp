package gleroy.com.mybaseapplication.presentation.viewmodel

import android.app.Application
import gleroy.com.mybaseapplication.data.remote.api.parameter.GetTodosRequestParam
import gleroy.com.mybaseapplication.data.remote.entity.Todo
import gleroy.com.mybaseapplication.data.repository.TodoRepository
import gleroy.com.mybaseapplication.data.request.RequestParameter
import gleroy.com.mybaseapplication.presentation.viewmodel.base.ARecyclerViewModel
import gleroy.com.mybaseapplication.utils.react.BaseSchedulerProvider
import javax.inject.Inject

class TodosViewModel @Inject constructor(application: Application,
                                         repository: TodoRepository,
                                         schedulerProvider: BaseSchedulerProvider) : ARecyclerViewModel<Todo>(application, repository, schedulerProvider) {

    override fun createFetchAllParameter(forceRefresh: Boolean): RequestParameter {
        return GetTodosRequestParam()
    }

}