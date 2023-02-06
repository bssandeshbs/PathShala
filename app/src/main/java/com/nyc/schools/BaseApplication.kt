package com.nyc.schools

import android.app.Application
import com.nyc.schools.di.AppComponentImpl
import com.nyc.schools.di.DaggerAppComponentImpl
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class BaseApplication: Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    lateinit var appComponent: AppComponentImpl

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponentImpl.builder().application(this).build()
        appComponent.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }
}