package gleroy.com.mybaseapplication.data.remote.api.base.exception.analyser

import gleroy.com.mybaseapplication.data.remote.entity.error.ErrorModel

interface IErrorAnalyser {

    fun handleAPIerror(errors: List<ErrorModel>): Boolean
}
