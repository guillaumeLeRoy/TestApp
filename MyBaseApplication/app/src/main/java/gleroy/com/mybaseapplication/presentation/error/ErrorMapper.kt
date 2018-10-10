package gleroy.com.mybaseapplication.presentation.error

import android.content.Context
import gleroy.com.mybaseapplication.presentation.viewmodel.state.Error

object ErrorMapper {

    fun createMessageFromError(context: Context, error: Error): String {
        return "always the same error message for now" //todo map error with a correct error message
    }
}