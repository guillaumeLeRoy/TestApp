package gleroy.com.mybaseapplication.di.module;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import gleroy.com.mybaseapplication.di.scope.ViewModelKey;
import gleroy.com.mybaseapplication.presentation.viewmodel.MyViewModel;
import gleroy.com.mybaseapplication.presentation.viewmodel.base.ViewModelFactory;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MyViewModel.class)
    abstract ViewModel bindMyViewModel(MyViewModel viewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
