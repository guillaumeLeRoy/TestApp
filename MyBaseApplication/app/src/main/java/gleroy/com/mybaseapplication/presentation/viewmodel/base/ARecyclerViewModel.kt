package gleroy.com.mybaseapplication.presentation.viewmodel.base

import android.app.Application
import android.arch.lifecycle.LiveData
import android.support.annotation.MainThread
import gleroy.com.mybaseapplication.data.repository.base.IRepository
import gleroy.com.mybaseapplication.data.request.RequestParameter
import gleroy.com.mybaseapplication.presentation.viewmodel.state.Error
import gleroy.com.mybaseapplication.utils.livedata.FirstNotifierLiveData
import gleroy.com.mybaseapplication.utils.react.BaseSchedulerProvider
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit

/**
 * Class to handle pull to refresh and load next pages when user scrolls to the bottom.
 */
abstract class ARecyclerViewModel<T>(application: Application,
                                     private val repository: IRepository<T>,
                                     private val schedulerProvider: BaseSchedulerProvider) : AStatesViewModel<List<T>>(application), FirstNotifierLiveData.Listener {

    class DisposableWithAction<T>(val disposable: Disposable, val onSuccess: ((t: List<T>) -> Unit)?)

    private var disposables: CompositeDisposable = CompositeDisposable()
    private var fetchDisposable: DisposableWithAction<T>? = null
    private val entitiesLiveData = FirstNotifierLiveData<Result<T>>(WeakReference(this))

    class Result<out T>(val data: List<T>?, val request: RequestParameter)

    var autoRefreshWhenObserved = true

    val all: LiveData<Result<T>>
        get() = entitiesLiveData

    /* ------ abstract methods ----- */

    protected abstract fun createFetchAllParameter(forceRefresh: Boolean): RequestParameter

    /* ------ abstract methods ----- */

    override fun onFirstObserverRegistered() {
        Timber.d("onFirstObserverRegistered")
        disposables.add(observeLoadingState())
        if (autoRefreshWhenObserved) {
            fetchAll()
        }
    }

    override fun onCleared() {
        Timber.d("onCleared")
        clearData()
        stop()
        super.onCleared()
    }

    private fun stop() {
        disposables.clear()
    }

    fun clearData() {
        Timber.d("clearData")
        entitiesLiveData.value = null
    }

    override fun onRefreshRequested(onSuccess: ((t: List<T>) -> Unit)?) {
        fetchAll(true, onSuccess)
    }

    @MainThread
    fun onRefreshRequested(forceRefresh: Boolean, onSuccess: ((t: List<T>) -> Unit)? = null) {
        Timber.d("onRefreshRequested")
        fetchAll(forceRefresh, onSuccess)
    }

    private fun onListReceived(entities: List<T>, request: RequestParameter) {
        Timber.d("onListReceived : ${entities.size}")
        updateState(entities.isEmpty())
        entitiesLiveData.value = Result(entities, request)
    }

    private fun updateState(isEmpty: Boolean) {
        if (isEmpty) {
            changeToEmptyState()
        } else {
            changeToMainState()
        }
    }

    private fun onErrorReceived(throwable: Throwable) {
        handleError(throwable)
    }

    protected open fun handleError(throwable: Throwable) {
        /* if not empty then only display a snackbar with this error
         if empty then display the error state
       */
        val result = entitiesLiveData.value
        val errorState = Error(throwable)
        if (result?.data == null || result.data.isEmpty()) {
            changeToErrorState(errorState)
        } else {
            displaySnackBar(errorState)
        }
    }

    /*-------------- private methods -------------------*/

    private fun observeLoadingState(): Disposable {
        Timber.d("observeLoadingState")
        return Single.just(false).subscribe { isLoading -> updateLoadingState(isLoading) }
//        return repository
//                .isLoadingAll
//                .subscribe { isLoading -> updateLoadingState(isLoading) }
    }

    private fun fetchAll(forceRefresh: Boolean = false, onSuccess: ((t: List<T>) -> Unit)? = null) {
        fetchAll(createFetchAllParameter(forceRefresh), onSuccess)
    }

    private fun fetchAll(parameter: RequestParameter, onSuccess: ((t: List<T>) -> Unit)?) {
        Timber.d("fetchAll, parameter: $parameter, onSuccess: $onSuccess")
        val prevDisposable = fetchDisposable
        // don't cancel the request if it has an action
        if (prevDisposable != null && prevDisposable.onSuccess == null) {
            disposeFetchAllDisposable()
        }

        // keep a ref to avoid multiple fetches at the same time and use delaySubscription
        val scheduler = schedulerProvider.ui()
        val disposable = repository
                .fetchAll(parameter)
                .delaySubscription(DELAY_MILL_SEC, TimeUnit.MILLISECONDS, scheduler)
                .doOnSubscribe { Timber.d("fetchAll, starts") }
                .subscribe({ entities ->
                    Timber.d("fetchAll, complete")
                    onListReceived(entities, parameter)
                    if (onSuccess != null) {
                        onSuccess(entities)
                    }
                }, {
                    Timber.d("fetchAll, error")
                    this.onErrorReceived(it)
                })
        fetchDisposable = DisposableWithAction(disposable, onSuccess)
        disposable?.let { disposables.addAll(it) }
    }

    private fun disposeFetchAllDisposable() {
        fetchDisposable?.let {
            Timber.d("disposeFetchAllDisposable")
            it.disposable.dispose()
            disposables.remove(it.disposable)
        }
    }

    companion object {
        const val DELAY_MILL_SEC = 100L
    }

}
