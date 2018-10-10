package gleroy.com.mybaseapplication.data.remote.api.base

import com.google.gson.GsonBuilder
import gleroy.com.mybaseapplication.data.remote.api.base.error.*
import gleroy.com.mybaseapplication.data.remote.base.RequestUtils
import gleroy.com.mybaseapplication.data.remote.base.RequestsGenerator
import gleroy.com.mybaseapplication.presentation.error.ErrorMapper
import io.reactivex.subjects.MaybeSubject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import timber.log.Timber
import java.net.HttpURLConnection
import java.util.ArrayList

abstract class BaseApiRequest<Result>(private val requestUtils: RequestUtils) {

    @Throws(Exception::class)
    abstract fun executeRequest(requestsGenerator: RequestsGenerator): Call<Result>

    private var RxResponse: MaybeSubject<Result> = MaybeSubject.create<Result>()

    fun execute() {

        val call = executeRequest(requestUtils.requestsGenerator)

        //catch any exception that could happen during the call
        var response: Response<Result>? = null
        try {
            response = call.execute()
            processResponse(response!!)
        } catch (e: Exception) {
            Timber.e(e, "onRun, exception")
            if (e is AJobException) {
                //if we already handled this exception ( ie an AJobException was created ) just throw it again
                throw e
            }

            /* sth unexpected happened, create a new generic exception */

            //try to read the status code
            var statusCode: Int? = null
            if (response != null) {
                statusCode = response.code()
            }

            //then more details
            var method: String? = null
            var url: String? = null

            try {
                Timber.e("onRun, exception, call: $call")
                if (call.request() != null) {
                    method = call.request().method()
                    if (call.request().url() != null) {
                        url = call.request().url().encodedPath()
                    }
                }
            } catch (internal: Exception) {
                Timber.d(internal, "onRun internal exception")
            }

            //create the exception
            throw CallException(e, statusCode, method, url)
        }
    }

    @Throws(Throwable::class)
    private fun processResponse(response: Response<Result>) {
        //retain response
        mResponse = response

        val statusCode = response.code()
        val method = response.raw().request().method()
        val url = response.raw().request().url().encodedPath()

        Timber.d("processResponse, status code : $statusCode")
        Timber.d("processResponse, method : $method")

        if (statusCode == HttpURLConnection.HTTP_CREATED || statusCode == HttpURLConnection.HTTP_OK) {
            Timber.d("processResponse, status code OK")

            val result = response.body()
            Timber.d("processResponse, result : " + result!!)

            // only log for a GET request
            if (result == null && "GET" == method) {
                Timber.d("processResponse, NULL result or GET method")
                //throw exception as for a GET we should receive sth
                throw EmptyResponseException(statusCode, method, url) //todo to change
            }

            //status says it's OK
            try {
                if (result == null) {
                    RxResponse.onComplete()
                } else {
                    RxResponse.onSuccess(result)
                }
            } catch (e: Exception) {
                Timber.e(e, "onSuccess exception")
                throw SuccessParsingException(statusCode, method, url) //todo to change
            }

        } else {
            //status NOT ok
            Timber.d("processResponse, status code NOT ok")

            val error = response.errorBody()
            if (error != null) {
                //read any server's error
                val t = readErrorResponse(response, error)
                Timber.d("processResponse, throw readErrorResponse")
                throw t //todo to change
            } else {
                throw EmptyResponseException(statusCode, method, url)//todo to change
            }

        }
        Timber.d("processResponse finished")
    }

    /**
     * parse the error from the http response
     *
     * @param responseBody [ResponseBody] body response from API
     * @return [Throwable] error with the data parsed if it was possible
     */
    protected fun readErrorResponse(response: Response<Result>, responseBody: ResponseBody): Throwable {
        Timber.d("readErrorResponse")

        val statusCode = response.code()
        val method = response.raw().request().method()
        val url = response.raw().request().url().encodedPath()

        try {
            return analyseErrorResponse(statusCode, method, url, responseBody.string())
        } catch (throwable: Throwable) {
            Timber.d(throwable, "readErrorResponse, exception")
            //if we couldn't parse the error, return an ErrorParsingException
            return ErrorParsingException(statusCode, method, url, null)
        }

    }

    @Throws(Throwable::class)
    protected fun analyseErrorResponse(statusCode: Int, method: String?, url: String?, error: String?): Throwable {
        Timber.d("analyseErrorResponse : " + error!!)

        return if (error != null && error.length > 0) {

            //try to parse XML error
            try {
                //We parse the error from the backend
                val errorsData = SerializerRequest.errorsDataSerializer(error)
                val mapper = ErrorMapper()
                val errorsDomain = mapper.map(errorsData)

                val errors = errorsDomain.getErrors()

                ErrorResponseException(statusCode, method, url, errorsDomain)
            } catch (t_xml: Throwable) {
                //couldn't parse using XML, try to parse JSON
                try {
                    val gsonBuilder = GsonBuilder()
                    val gson = gsonBuilder.create()
                    val errorJson = gson.fromJson(error, Error::class.java)

                    //convert to error model
                    val errors = ArrayList<ErrorModel>()
                    errors.add(ErrorModel(errorJson.code, errorJson.message))
                    val errorsDomain = ErrorsModel(errors)

                    ErrorResponseException(statusCode, method, url, errorsDomain)
                } catch (e: Throwable) {
                    Timber.d(e, "parseErrorResponse, exception parsing JSON")
                    //if we couldn't parse the error, return an ErrorParsingException
                    ErrorParsingException(statusCode, method, url, error)
                }
            }

        } else {
            EmptyResponseException(statusCode, method, url)
        }

    }

    protected fun onCancel(cancelReason: Int, throwable: Throwable?) {
        Timber.d("onCancel, cancelReason : " + cancelReason + ", throwable : " + throwable + ", this = " + this)

        //wrap exception around a {@link AJobException}
        val jobException: AJobException

        if (throwable == null) {
            //make sure we always have an {@link AJobException}
            jobException = CallException( null, null, null, null)
        } else if (throwable is AJobException) {
            jobException = throwable
        } else {
            //make sure we always have an {@link AJobException}
            jobException = CallException( throwable, null, null, null)
        }

        //don't log if the job was cancelled by user action.
        if (cancelReason != CancelReason.CANCELLED_WHILE_RUNNING) {
            //log exception
            logException(jobException)
        }

        //if the job was canceled in background. we will not send the response to UI for the job that was running
        if (!isCancelled()) {
            RxResponse.onError(jobException)
        }
    }

}