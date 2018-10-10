package gleroy.com.mybaseapplication

import android.app.Activity
import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import gleroy.com.mybaseapplication.di.component.AppComponent
import gleroy.com.mybaseapplication.di.component.DaggerAppComponent
import gleroy.com.mybaseapplication.di.module.AppModule
import gleroy.com.mybaseapplication.di.module.NetModule
import timber.log.Timber
import javax.inject.Inject

class MyApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        val appComponent = createComponent()
        appComponent.inject(this)

        Timber.plant(Timber.DebugTree())

    }

    override fun activityInjector(): AndroidInjector<Activity>? {
        return activityInjector
    }

    /**
     * Override this method to load a different component
     *
     * @return
     */
    protected fun createComponent(): AppComponent {
        val builder = DaggerAppComponent.builder()
        builder.netModule(NetModule())
        builder.appModule(AppModule(this))
        return builder.build()
    }
}
