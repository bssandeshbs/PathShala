package com.nyc.schools.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nyc.schools.ViewModelProviderFactory
import com.nyc.schools.ui.SchoolListFragment
import com.nyc.schools.ui.MainActivity
import com.nyc.schools.ui.SchoolViewModel
import com.nyc.schools.ui.SchoolScoresFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.multibindings.IntoMap

/*
 NYCSchoolModule provides all fragment and viewModels related to NYCSchoolModule
 */
@Module
abstract class NYCSchoolModule {

    @MainScope
    @ContributesAndroidInjector(modules = [AndroidSupportInjectionModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [AndroidSupportInjectionModule::class])
    abstract fun contributeSchoolListFragment(): SchoolListFragment

    @ContributesAndroidInjector(modules = [AndroidSupportInjectionModule::class])
    abstract fun contributeSchoolScoresFragment(): SchoolScoresFragment

    @Binds
    abstract fun bindViewModelFactory(factoryModule: ViewModelProviderFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SchoolViewModel::class)
    abstract fun bindSchoolListViewModel(viewModel: SchoolViewModel): ViewModel
}