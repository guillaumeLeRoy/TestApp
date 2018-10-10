package gleroy.com.mybaseapplication.presentation.fragment.base

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import gleroy.com.mybaseapplication.R
import gleroy.com.mybaseapplication.presentation.adapter.decorator.HorizontalItemDecorator
import gleroy.com.mybaseapplication.presentation.viewmodel.base.ARecyclerViewModel

/**
 * Base Fragment class to handle a RecyclerView. Work in association with a [ARecyclerViewModel]
 */
abstract class ARecyclerFragment<VM : ARecyclerViewModel<*>> : AStatesFragment<VM>() {

    /*-------------- abstract methods -------------------*/

    abstract val totalItemCount: Int

    /*-------------- abstract methods end -------------------*/

    protected lateinit var rv: RecyclerView

    override val mainStateFragmentLayout: Int
        get() = R.layout.fragment_recycler

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureRecyclerView(view)
    }

    open fun configureRecyclerView(view: View) {
        rv = view.findViewById(R.id.ui_recycler_view)
        rv.layoutManager = LinearLayoutManager(context)
        addAnimatorAndDecorator()
    }

    open fun addAnimatorAndDecorator() {
        rv.itemAnimator = DefaultItemAnimator()
        rv.setHasFixedSize(true)
        rv.addItemDecoration(getItemDecoration())
    }

    open fun getItemDecoration(): RecyclerView.ItemDecoration {
        return HorizontalItemDecorator(context?.resources?.getDimensionPixelOffset(R.dimen.app_padding_small)
                ?: 0)
    }
}
