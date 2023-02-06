package com.nyc.schools

import android.content.Context
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

open class BaseFragment: Fragment() {

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }
}