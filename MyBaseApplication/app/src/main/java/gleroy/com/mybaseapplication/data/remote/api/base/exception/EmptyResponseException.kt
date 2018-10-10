package gleroy.com.mybaseapplication.data.remote.api.base.exception

/**
 * Exception raised when the response is empty
 */
class EmptyResponseException(code: Int?, method: String?, url: String?) : AJobException(code, method, url)
