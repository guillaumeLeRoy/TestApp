package gleroy.com.mybaseapplication.presentation.fragment.base

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import timber.log.Timber
import javax.inject.Inject

abstract class ABaseFragment<VM : ViewModel> : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    // this VM is specific to that fragment and not shared
    protected lateinit var viewModel: VM

    /* --- abstract methods ----*/

    protected abstract fun getViewModel(factory: ViewModelProvider.Factory): VM

    /* --- abstract methods ----*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = getViewModel(viewModelFactory)
    }

    /**
     * Override this method if you want to be notified when back key is pressed.
     *
     * @return true if action was handled
     */
    open fun onBackPressed(): Boolean {
        return dispatchBackPressedToChildFragments()
    }

    /**
     * @return true if the action was handled by at least one child fragment
     */
    private fun dispatchBackPressedToChildFragments(): Boolean {
        Timber.d("dispatchBackPressedToChildFragments, this : " + this)
        var wasBackHandled = false
        val fragments = childFragmentManager.fragments
        if (fragments != null) {
            for (fragment in fragments) {
                Timber.d("dispatchBackPressedToChildFragments, frag : $fragment")
                if (fragment is ABaseFragment<*> && fragment.isVisible) wasBackHandled = wasBackHandled or fragment.onBackPressed()
            }
        }
        Timber.d("dispatchBackPressedToChildFragments, wasBackHandled : $wasBackHandled")
        return wasBackHandled
    }
}
