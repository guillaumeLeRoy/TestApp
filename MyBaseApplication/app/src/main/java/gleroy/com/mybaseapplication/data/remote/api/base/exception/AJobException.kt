package gleroy.com.mybaseapplication.data.remote.api.base.exception

import timber.log.Timber

abstract class AJobException(val statusCode: Int?, val method: String?, val url: String?) : RuntimeException() {

    val logLabels: String
        get() {
            val strb = StringBuilder()

            if (statusCode != null) {
                if (strb.isNotEmpty()) {
                    strb.append("|")
                }

                strb.append("code:").append(statusCode)
            }

            if (method != null) {
                if (strb.isNotEmpty()) {
                    strb.append("|")
                }
                strb.append("method:").append(method)
            }

            if (url != null) {
                if (strb.isNotEmpty()) {
                    strb.append("|")
                }
                strb.append("url:").append(url)
            }

            return strb.toString()
        }

    init {
        Timber.d("mStatusCode : " + statusCode + ", mMethod : " + this.method + ", url : " + this.url)
    }

}
