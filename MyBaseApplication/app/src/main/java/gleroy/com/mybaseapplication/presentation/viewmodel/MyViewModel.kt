package gleroy.com.mybaseapplication.presentation.viewmodel

import android.app.Application
import gleroy.com.mybaseapplication.data.local.entity.MyLocalObject
import gleroy.com.mybaseapplication.data.remote.api.parameter.MyRequestParam
import gleroy.com.mybaseapplication.data.repository.MyRepository
import gleroy.com.mybaseapplication.data.request.RequestParameter
import gleroy.com.mybaseapplication.presentation.viewmodel.base.ADataLoaderViewModel
import timber.log.Timber
import javax.inject.Inject

class MyViewModel @Inject constructor(application: Application, repository: MyRepository) : ADataLoaderViewModel<MyLocalObject>(application, repository) {

    override fun createFetchParameter(forceRefresh: Boolean): RequestParameter {
        Timber.d("createFetchParameter")
        return MyRequestParam()
    }

    init {
        changeToMainState()
    }
}