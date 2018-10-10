package gleroy.com.mybaseapplication.utils

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import java.lang.ref.WeakReference

class FirstNotifierLiveData<T>(private val listener: WeakReference<Listener>?) : MutableLiveData<T>() {

    interface Listener {
        fun onFirstObserverRegistered()
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<T>) {
        val hadObs = hasObservers()
        super.observe(owner, observer)
        if (!hadObs) {
            listener?.get()?.onFirstObserverRegistered()
        }
    }
}