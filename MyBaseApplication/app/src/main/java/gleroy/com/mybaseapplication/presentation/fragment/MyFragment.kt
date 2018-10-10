package gleroy.com.mybaseapplication.presentation.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import dagger.android.support.AndroidSupportInjection
import gleroy.com.mybaseapplication.R
import gleroy.com.mybaseapplication.presentation.fragment.base.AStatesFragment
import gleroy.com.mybaseapplication.presentation.viewmodel.MyViewModel
import timber.log.Timber

class MyFragment : AStatesFragment<MyViewModel>() {

    override val mainStateFragmentLayout: Int
        get() = R.layout.fragment_main_state_my_fragment

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun getViewModel(factory: ViewModelProvider.Factory): MyViewModel {
        return ViewModelProviders.of(this, factory).get(MyViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.data.observe(this, Observer { Timber.d("observe") })
    }

}