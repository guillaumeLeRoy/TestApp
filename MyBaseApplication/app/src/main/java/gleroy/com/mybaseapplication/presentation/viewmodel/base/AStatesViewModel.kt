package gleroy.com.mybaseapplication.presentation.viewmodel.base

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.support.annotation.MainThread
import android.support.annotation.StringRes
import gleroy.com.mybaseapplication.presentation.viewmodel.state.Error
import gleroy.com.mybaseapplication.presentation.viewmodel.state.ViewState
import gleroy.com.mybaseapplication.utils.SingleLiveEvent
import timber.log.Timber

abstract class AStatesViewModel<T>(application: Application) : AndroidViewModel(application) {

    private val initState = ViewState.InitState()
    private val mainState = ViewState.MainState()
    private val emptyState = ViewState.EmptyState()

    private val state = MutableLiveData<ViewState>()

    private val loading = SingleLiveEvent<Boolean>()

    private val displaySnackBar = SingleLiveEvent<Error>()

    val loadingState: LiveData<Boolean>
        get() = loading

    val snackbar: LiveData<Error>
        get() = displaySnackBar

    /*-------------- abstract methods -------------------*/


    abstract fun onRefreshRequested(onSuccess: ((t: T) -> Unit)? = null)

    /*-------------- abstract methods -------------------*/


    fun getState(): LiveData<ViewState> {
        return state
    }

    /**
     * Reset to the initial state which is [ViewState.InitState]
     */
    @MainThread
    fun reset() {
        Timber.d("reset")
        state.value = initState
    }

    @MainThread
    fun changeToEmptyState() {
        switchToState(emptyState)
    }

    /**
     * Try to detect if state may have changed and if so will try to switch from current state to the new state.
     */
    @MainThread
    fun changeToErrorState(error: Error) {
        Timber.d("changeToErrorState")
        switchToState(ViewState.ErrorState(error))
    }

    @MainThread
    fun changeToMainState() {
        switchToState(mainState)
    }

    @MainThread
    fun updateLoadingState(isLoading: Boolean) {
        Timber.d("updateLoadingState,isLoading: $isLoading")
        this.loading.value = isLoading
    }

    @MainThread
    fun displaySnackBar(@StringRes resId: Int) {
        displaySnackBar(Error(Throwable(getApplication<Application>().getString(resId))))
    }

    @MainThread
    fun displaySnackBar(errorState: Error) {
        Timber.d("displaySnackBar")
        displaySnackBar.value = errorState
    }

    /**
     * Switch to the given state.
     *
     * @param newState new state to switch to
     */
    @MainThread
    private fun switchToState(newState: ViewState) {
        Timber.d("switchToState, state : $newState")

        val oldState = state.value
        if (oldState !== newState) {
            state.setValue(newState)
        } else {
            Timber.d("switchToState, BLOCK")
        }
    }

}