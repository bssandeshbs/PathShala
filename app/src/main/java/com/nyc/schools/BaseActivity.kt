package com.nyc.schools

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity(), HasAndroidInjector {
    var mProgressBar: ProgressBar? = null

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        androidInject()
        super.onCreate(savedInstanceState)
    }

    fun androidInject() {
        AndroidInjection.inject(this)
    }

    override fun setContentView(layoutResID: Int) {
        val constraintLayout =
            layoutInflater.inflate(R.layout.activity_base, null) as ConstraintLayout
        val frameLayout = constraintLayout.findViewById<FrameLayout>(R.id.activity_content)
        mProgressBar = constraintLayout.findViewById(R.id.progress_bar)
        layoutInflater.inflate(layoutResID, frameLayout, true)
        super.setContentView(constraintLayout)

    }

    open fun showProgressBar(visibility: Boolean) {
        mProgressBar!!.visibility = if (visibility) View.VISIBLE else View.GONE
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

}