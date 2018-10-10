package gleroy.com.mybaseapplication.di.module;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import gleroy.com.mybaseapplication.presentation.activity.MainActivity;
import gleroy.com.mybaseapplication.presentation.fragment.MyFragment;

@Module
public abstract class AndroidBindingModule {

    ////////////////////////////////////
    // Activity
    ////////////////////////////////////

    @ContributesAndroidInjector
    abstract MainActivity MainActivity();

    ////////////////////////////////////
    // Fragments
    ////////////////////////////////////

    @ContributesAndroidInjector
    abstract MyFragment MyFragment();

}