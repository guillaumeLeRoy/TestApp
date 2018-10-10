package gleroy.com.mybaseapplication.data.remote.api.base.exception


/**
 * Exception raised when the parsing of the successful response failed
 */
class SuccessParsingException(code: Int?, method: String?, url: String?) : AJobException(code, method, url)
