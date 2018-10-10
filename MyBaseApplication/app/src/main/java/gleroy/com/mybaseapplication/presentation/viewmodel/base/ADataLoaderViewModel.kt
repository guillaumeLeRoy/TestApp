package gleroy.com.mybaseapplication.presentation.viewmodel.base

import android.app.Application
import android.arch.lifecycle.LiveData
import android.support.annotation.MainThread
import gleroy.com.mybaseapplication.data.repository.base.IRepository
import gleroy.com.mybaseapplication.data.request.RequestParameter
import gleroy.com.mybaseapplication.presentation.viewmodel.state.Error
import gleroy.com.mybaseapplication.utils.livedata.FirstNotifierLiveData
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber
import java.lang.ref.WeakReference

abstract class ADataLoaderViewModel<T>(application: Application,
                                       protected open val repository: IRepository<T>) : AStatesViewModel<T>(application), FirstNotifierLiveData.Listener {

    private var disposables: CompositeDisposable = CompositeDisposable()
    private val entityLiveData = FirstNotifierLiveData<T>(WeakReference(this))

    /* ------ abstract methods ----- */

    protected abstract fun createFetchParameter(forceRefresh: Boolean): RequestParameter

    /* ------ abstract methods ----- */

    val data: LiveData<T>
        get() = entityLiveData

    override fun onCleared() {
        super.onCleared()
        Timber.d("onCleared")
        disposables.clear()
    }

    @MainThread
    override fun onRefreshRequested(onSuccess: ((t: T) -> Unit)?) {
        Timber.d("onRefreshRequested")
        fetch(true, onSuccess)
    }

    override fun onFirstObserverRegistered() {
        Timber.d("onFirstObserverRegistered")
        disposables.add(observeLoadingState())
        fetch(false)
    }

    open fun onDataReceived(entity: T?) {
        Timber.d("onDataReceived")
        entityLiveData.value = entity
    }

    /*-------------- private methods -------------------*/

    private fun observeLoadingState(): Disposable {
        Timber.d("observeLoadingState")
        return Single.just(false).subscribe { isLoading -> updateLoadingState(isLoading) }
        // todo : to fix
//        return repository
//                .isLoading
//                .subscribe { isLoading -> updateLoadingState(isLoading) }
    }

    private fun fetch(forceRefresh: Boolean, onSuccess: ((t: T) -> Unit)? = null) {
        try {
            disposables.add(fetch(createFetchParameter(forceRefresh), onSuccess))
        } catch (e: Exception) {
            updateLoadingState(false)
            onErrorReceived(e)
        }
    }

    private fun fetch(parameter: RequestParameter, onSuccess: ((t: T) -> Unit)? = null): Disposable {
        Timber.d("fetch, parameter: $parameter")
        return repository
                .fetch(parameter)
                .doOnComplete { onResponseReceived(null) }
                .subscribe({ entity ->
                    Timber.d("fetch, get result")
                    if (onSuccess != null) {
                        onSuccess(entity)
                    } else {
                        onResponseReceived(entity)
                    }
                }, { this.onErrorReceived(it) })
    }

    private fun onResponseReceived(entity: T?) {
        Timber.d("onResponseReceived : $entity")
        if (entity == null) {
            changeToEmptyState()
        } else {
            changeToMainState()
        }
        onDataReceived(entity)
    }

    private fun onErrorReceived(throwable: Throwable) {
        Timber.e(throwable, "onErrorReceived, error")
        /* if not empty then only display a snackbar with this error
           if empty then display the error state
         */
        val entity = entityLiveData.value
        val errorState = Error(throwable)
        if (entity == null) {
            changeToErrorState(errorState)
        } else {
            displaySnackBar(errorState)
        }

    }
}
