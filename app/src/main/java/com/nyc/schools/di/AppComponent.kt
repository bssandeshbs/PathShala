package com.nyc.schools.di

import android.app.Application
import com.nyc.schools.BaseApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, NYCSchoolModule::class, CoreModule::class, SchoolModule::class])
interface AppComponentImpl : AndroidInjector<DaggerApplication> {

    fun inject(baseApplication: BaseApplication)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponentImpl
    }
}