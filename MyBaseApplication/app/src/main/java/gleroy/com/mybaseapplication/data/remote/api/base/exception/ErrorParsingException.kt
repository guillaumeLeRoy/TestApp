package gleroy.com.mybaseapplication.data.remote.api.base.exception

/**
 * Exception raised when failing to parse error response
 */
class ErrorParsingException(code: Int?, method: String?, url: String?, private val receivedError: String?) : AJobException(code, method, url)
