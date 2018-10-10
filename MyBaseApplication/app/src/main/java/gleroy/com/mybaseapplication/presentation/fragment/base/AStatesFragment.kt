package gleroy.com.mybaseapplication.presentation.fragment.base

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.annotation.MainThread
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import gleroy.com.mybaseapplication.R
import gleroy.com.mybaseapplication.presentation.error.ErrorMapper
import gleroy.com.mybaseapplication.presentation.viewmodel.base.AStatesViewModel
import gleroy.com.mybaseapplication.presentation.viewmodel.state.Error
import gleroy.com.mybaseapplication.presentation.viewmodel.state.ViewState
import gleroy.com.mybaseapplication.utils.Constants
import timber.log.Timber

/**
 * Fragment responsible for handling multiple states.
 */
abstract class AStatesFragment<VM : AStatesViewModel<*>> : ABaseFragment<VM>() {

    /*-------------- abstract methods -------------------*/

    /**
     * Get Main layout for your view
     *
     * @return your main layout
     */
    abstract val mainStateFragmentLayout: Int


    /*-------------- abstract methods end -------------------*/

    private lateinit var uiSwipeRefresh: SwipeRefreshLayout

    /**
     * Container for our different states
     */
    private lateinit var stateContainer: ViewGroup

    /**
     *
     * View for our error state
     */
    private var uiErrorContainer: View? = null


    private fun createErrorContainer() {
        if (uiErrorContainer == null) {
            val view = layoutInflater.inflate(errorStateFragmentLayout, stateContainer, false)
            uiErrorContainer = view
            stateContainer.addView(view)
        }
    }

    /**
     * View for our main state
     */
    private var uiMainContentContainer: View? = null

    private fun createMainContainer() {
        if (uiMainContentContainer == null) {
            val view = layoutInflater.inflate(mainStateFragmentLayout, stateContainer, false)
            uiMainContentContainer = view
            stateContainer.addView(view)
        }
    }

    /**
     * View for our empty state
     */
    private var uiEmptyContainer: View? = null

    private fun createEmptyContainer() {
        if (uiEmptyContainer == null) {
            val view = layoutInflater.inflate(emptyStateFragmentLayout, stateContainer, false)
            uiEmptyContainer = view
            stateContainer.addView(view)
        }
    }

    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        Timber.d("onRefresh, uiSwipeRefresh.isRefreshing() : " + uiSwipeRefresh.isRefreshing)
        if (uiSwipeRefresh.isRefreshing) {
            viewModel.onRefreshRequested()
        }
    }

    /*------------------ states layout ------------*/

    protected open val fragmentLayout: Int
        get() = R.layout.fragment_states_layout_vm

    /**
     * Override this method to display your own empty layout.
     *
     * @return default empty layout
     */
    protected open val emptyStateFragmentLayout: Int
        get() = R.layout.fragment_state_empty_layout

    /**
     * Override this method to display your own error layout.
     *
     * @return default error layout
     */
    protected open val errorStateFragmentLayout: Int
        get() = R.layout.fragment_state_error_layout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Timber.d("onCreateView")

        val root = inflater.inflate(fragmentLayout, container, false)
        //global swipe to refresh
        uiSwipeRefresh = root.findViewById(R.id.ui_swipe_refresh)
        stateContainer = root.findViewById(R.id.states_container)
        createMainContainer()
        uiMainContentContainer?.visibility = View.GONE

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Timber.d("onViewCreated")

        uiSwipeRefresh.setOnRefreshListener(onRefreshListener)
        uiSwipeRefresh.setColorSchemeResources(*Constants.SWIPE_TO_REFRESH_COLORS)

        viewModel.getState().observe(this, Observer { state ->
            Timber.d("onChanged")
            state?.let {
                updateState(it)
            }
        })

        viewModel.loadingState.observe(this, Observer { isLoading ->
            isLoading?.let {
                updateLoader(it)
            }
        })

        viewModel.snackbar.observe(this, Observer { errorState ->
            Timber.d("snackBar, onChanged")
            errorState?.let {
                displaySnackBar(it)
            }
        })

    }

    override fun onDestroyView() {
        uiMainContentContainer = null
        uiEmptyContainer = null
        uiErrorContainer = null
        super.onDestroyView()
    }

    /**
     * Override this method to do sth when main view is displayed
     *
     * @param view main view
     */
    protected fun onMainViewDisplayed(view: View) {}

    /**
     * Override this method to do sth when empty view is displayed
     *
     * @param view empty view
     */
    protected open fun onEmptyViewDisplayed(view: View) {}

    /**
     * Override this method to do sth when error view is displayed
     *
     * @param view  view error view
     * @param error received error
     */
    protected open fun onErrorViewDisplayed(view: View, error: Error) {
        context?.let {
            //we know we are using the default empty layout and so we can find this TextView
            val message = view.findViewById<TextView>(R.id.error_content)
            val errMsg = ErrorMapper.createMessageFromError(it, error)
            message.text = errMsg
        }

        val retry = view.findViewById<View>(R.id.ui_error_retry)
        retry.setOnClickListener { _ ->
            Timber.d("onErrorViewDisplayed, retry clicked")
            viewModel.onRefreshRequested()
        }
    }

    @MainThread
    private fun updateState(state: ViewState) {
        Timber.d("updateState, state :$state")
        when (state) {
            is ViewState.MainState -> switchToMainState()
            is ViewState.EmptyState -> switchToEmptyState()
            is ViewState.ErrorState -> switchToErrorState(state)
            is ViewState.InitState -> switchToInitState()
        }
    }

    @MainThread
    private fun switchToInitState() {
        Timber.d("switchToLoadingState")

        updateMainContainerIfAttached(false)
        updateErrorContainerIfAttached(false)
        updateEmptyContainerIfAttached(false)
    }

    @MainThread
    private fun switchToMainState() {
        Timber.d("switchToMainState")

        updateMainContainerIfAttached(true)
        updateErrorContainerIfAttached(false)
        updateEmptyContainerIfAttached(false)
        uiMainContentContainer?.let { onMainViewDisplayed(it) }
    }

    @MainThread
    private fun switchToEmptyState() {
        Timber.d("switchToEmptyState")

        updateEmptyContainerIfAttached(true)
        updateMainContainerIfAttached(false)
        updateErrorContainerIfAttached(false)
        uiEmptyContainer?.let { onEmptyViewDisplayed(it) }
    }

    @MainThread
    private fun switchToErrorState(state: ViewState.ErrorState) {
        Timber.d("switchToErrorState")

        updateErrorContainerIfAttached(true)
        updateEmptyContainerIfAttached(false)
        updateMainContainerIfAttached(false)
        uiErrorContainer?.let { onErrorViewDisplayed(it, state.error) }
    }

    private fun updateMainContainerIfAttached(isVisible: Boolean) {
        createMainContainer()
        uiMainContentContainer?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun updateErrorContainerIfAttached(isVisible: Boolean) {
        createErrorContainer()
        uiErrorContainer?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun updateEmptyContainerIfAttached(isVisible: Boolean) {
        createEmptyContainer()
        uiEmptyContainer?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    /*----------------------PULL TO REFRESH------------*/

    internal fun updatePullToRefreshEnability(isEnable: Boolean) {
        uiSwipeRefresh.isEnabled = isEnable
    }

    private fun updateLoader(isLoading: Boolean) {
        Timber.d("updateLoader, isLoading : $isLoading")
        if (isLoading) {
            displayProgress()
        } else {
            hideProgress()
        }
    }

    private fun displayProgress() {
        displayPullToRefreshProgress()
    }

    private fun hideProgress() {
        hidePullToRefreshProgress()
    }

    private fun displayPullToRefreshProgress() {
        Timber.d("progress, displayPullToRefreshProgress")
        if (!uiSwipeRefresh.isRefreshing) {
            Timber.d("start pull to refresh")
            uiSwipeRefresh.isRefreshing = true
        }
    }

    private fun hidePullToRefreshProgress() {
        Timber.d("progress, hidePullToRefreshProgress")
        if (uiSwipeRefresh.isRefreshing) {
            Timber.d("stop pull to refresh")
            uiSwipeRefresh.isRefreshing = false
        }
    }

    /*----------------------PULL TO REFRESH------------*/

    /*------------ Error SnackBar------------*/

    fun displaySnackBar(error: Error) {
        context?.let {
            val msg = ErrorMapper.createMessageFromError(it, error)
            displaySnackBar(msg)
        }
    }

    fun displaySnackBar(text: String) {
        view?.let { Snackbar.make(it, text, Snackbar.LENGTH_LONG).show() }
    }

    fun displaySnackBar(textId: Int) {
        view?.let { Snackbar.make(it, textId, Snackbar.LENGTH_LONG).show() }
    }

}
