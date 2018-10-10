package gleroy.com.mybaseapplication.data.remote.api.base.exception

import gleroy.com.mybaseapplication.data.remote.entity.error.ErrorsModel

class ErrorResponseException(code: Int?,
                             method: String?,
                             url: String?,
                             val apIerror: ErrorsModel) : AJobException(code, method, url)
