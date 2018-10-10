package gleroy.com.mybaseapplication.presentation.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.FrameLayout
import android.widget.TextView
import dagger.android.support.AndroidSupportInjection
import gleroy.com.mybaseapplication.R
import gleroy.com.mybaseapplication.presentation.adapter.TodosRecyclerViewAdapter
import gleroy.com.mybaseapplication.presentation.fragment.base.ARecyclerFragment
import gleroy.com.mybaseapplication.presentation.viewmodel.TodosViewModel
import gleroy.com.mybaseapplication.presentation.viewmodel.base.ARecyclerViewModel
import gleroy.com.mybaseapplication.presentation.viewmodel.state.Error
import timber.log.Timber
import javax.inject.Inject

class TodosFragment : ARecyclerFragment<TodosViewModel>() {

    override val totalItemCount: Int
        get() = adapter.itemCount

    private lateinit var adapter: TodosRecyclerViewAdapter

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("onViewCreated")
        adapter = TodosRecyclerViewAdapter()
        rv.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("onActivityCreated")

        viewModel.all.observe(this, (Observer { result ->
            result?.let { adapter.setData(it.data) }
        }))
    }

    override fun onEmptyViewDisplayed(view: View) {
        Timber.d("onEmptyViewDisplayed")

        val container = view.findViewById<ViewGroup>(R.id.ui_empty_container)
        //we know we are using the default empty layout and so we can find this TextView
        val uiTitle = view.findViewById<TextView>(R.id.ui_empty_textview_title)
        //we know we are using the default empty layout and so we can find this TextView
        val uiContent = view.findViewById<TextView>(R.id.ui_empty_textview_content)

        uiTitle.text = "No todo"
        uiContent.text = "No todo"
        (container.layoutParams as FrameLayout.LayoutParams).gravity = Gravity.CENTER
    }

    override fun onErrorViewDisplayed(view: View, error: Error) {
        val container = view.findViewById<ViewGroup>(R.id.ui_error_container)
        val retry = view.findViewById<View>(R.id.ui_error_retry)
        (container.layoutParams as FrameLayout.LayoutParams).gravity = Gravity.CENTER
        retry.visibility = View.VISIBLE
        super.onErrorViewDisplayed(view, error)
    }

    override fun getViewModel(factory: ViewModelProvider.Factory): TodosViewModel {
        return ViewModelProviders.of(this, factory).get(TodosViewModel::class.java)
    }

}
